package org.esa.beam.framework.gpf.graph;

import com.thoughtworks.xstream.io.xml.xppdom.Xpp3Dom;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.gpf.internal.OperatorConfiguration;

import javax.media.jai.JAI;
import java.awt.Dimension;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The {@code GraphContext} holds the context for executing the {@link Graph} by the {@link GraphProcessor}.
 *
 * @author Norman Fomferra
 * @author Marco Peters
 * @see Graph
 * @see GraphProcessor
 * @since 4.1
 */
public class GraphContext {

    private Graph graph;
    private Logger logger;
    private Map<Node, NodeContext> nodeContextMap;
    private List<NodeContext> outputNodeContextList;
    private ArrayDeque<NodeContext> initNodeContextDeque;


    /**
     * Creates a GraphContext for the given {@code graph} and a {@code logger}.
     *
     * @param graph  the {@link Graph} to create the context for
     * @param logger a logger
     * @throws GraphException if the graph context could not be created
     */
    public GraphContext(Graph graph, Logger logger) throws GraphException {
        this.graph = graph;
        this.logger = logger;

        outputNodeContextList = new ArrayList<NodeContext>(graph.getNodeCount() / 2);

        nodeContextMap = new HashMap<Node, NodeContext>(graph.getNodeCount() * 2);
        for (Node node : graph.getNodes()) {
            nodeContextMap.put(node, new NodeContext(this, node));
        }

        initNodeContextDeque = new ArrayDeque<NodeContext>(graph.getNodeCount());
    }

    /**
     * Gets the {@link Graph} of this context.
     *
     * @return the {@link Graph}
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Gets the @link Logger} of this context.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the preferred tile size.
     *
     * @return the preferred tile size
     */
    public Dimension getPreferredTileSize() {
        return JAI.getDefaultTileSize();
    }

    /**
     * Sets the preferred tile size.
     *
     * @param preferredTileSize the preferred tile size
     */
    public void setPreferredTileSize(Dimension preferredTileSize) {
        JAI.setDefaultTileSize(preferredTileSize);
    }

    /**
     * Returns an array containing the output products generated by this graph's output
     * nodes, i.e. nodes that are not input to other nodes.
     *
     * @return an array containing the output products of this graph
     */
    public Product[] getOutputProducts() {
        Product[] products = new Product[outputNodeContextList.size()];
        for (int i = 0; i < products.length; i++) {
            products[i] = outputNodeContextList.get(i).getTargetProduct();
        }
        return products;
    }

    /**
     * Gets the number of {@link NodeContext}s marked as output.
     *
     * @return the number of output {@link NodeContext}s
     */
    public int getOutputCount() {
        int outputCount = 0;
        for (Node node : graph.getNodes()) {
            NodeContext nodeContext = getNodeContext(node);
            if (nodeContext.isOutput()) {
                outputCount++;
            }
        }
        return outputCount;
    }

    /**
     * Gets the {@link NodeContext}s in the reverse order as they were initialized.
     *
     * @return a deque of {@link NodeContext}s
     */
    public Deque<NodeContext> getInitNodeContextDeque() {
        return initNodeContextDeque;
    }

    /**
     * Gets the {@link NodeContext} of the given node.
     *
     * @param node the node to get the context for
     * @return the {@link NodeContext} of the given {@code node} or
     *         {@code null} if it's not contained in this context
     */
    NodeContext getNodeContext(Node node) {
        return nodeContextMap.get(node);
    }

    /**
     * Gets the output {@link NodeContext} at the given index.
     *
     * @param index the index
     * @return the {@link NodeContext} at the given index
     */
    NodeContext getOutputNodeContext(int index) {
        return outputNodeContextList.get(index);
    }

    /**
     * Gets all output {@link NodeContext}s of this {@code GraphContext}
     *
     * @return an array of all output {@link NodeContext}s
     */
    NodeContext[] getOutputNodeContexts() {
        return outputNodeContextList.toArray(new NodeContext[outputNodeContextList.size()]);
    }

    /**
     * Gets the number of bands within all target products of the output {@link NodeContext}s.
     *
     * @return the count of all output bands
     */
    int getTotalOutputBandCount() {
        int bandCount = 0;
        for (NodeContext nodeContext : outputNodeContextList) {
            bandCount += nodeContext.getTargetProduct().getNumBands();
        }
        return bandCount;
    }

    /**
     * Adds the given {@code nodeContext} to the list of output {@link NodeContext}s.
     *
     * @param nodeContext the {@link NodeContext} to add as output
     */
    void addOutputNodeContext(NodeContext nodeContext) {
        outputNodeContextList.add(nodeContext);
    }

    static OperatorConfiguration createOperatorConfiguration(Xpp3Dom xpp3Dom,
                                                             GraphContext graphContext,
                                                             Map<String, Object> map) {
        if (xpp3Dom == null) {
            return null;
        }
        Xpp3Dom config = new Xpp3Dom(xpp3Dom.getName());
        Set<OperatorConfiguration.Reference> references = new HashSet<OperatorConfiguration.Reference>(17);
        Xpp3Dom[] children = xpp3Dom.getChildren();

        for (Xpp3Dom child : children) {
            String reference = child.getAttribute("refid");
            if (reference != null) {
                String parameterName = child.getName();
                if (reference.contains(".")) {
                    String[] referenceParts = reference.split("\\.");
                    String referenceNodeId = referenceParts[0];
                    String propertyName = referenceParts[1];
                    Node node = graphContext.getGraph().getNode(referenceNodeId);
                    NodeContext referedNodeContext = graphContext.getNodeContext(node);
                    Operator operator = referedNodeContext.getOperator();
                    OperatorConfiguration.PropertyReference propertyReference = new OperatorConfiguration.PropertyReference(parameterName, propertyName, operator);
                    references.add(propertyReference);
                } else {
                    OperatorConfiguration.ParameterReference parameterReference = new OperatorConfiguration.ParameterReference(parameterName, map.get(reference));
                    references.add(parameterReference);
                }
            } else {
                config.addChild(child);
            }
        }

        return new OperatorConfiguration(config, references);
    }
}
