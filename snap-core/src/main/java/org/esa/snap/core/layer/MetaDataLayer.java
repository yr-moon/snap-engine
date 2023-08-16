package org.esa.snap.core.layer;

import com.bc.ceres.binding.PropertySet;
import com.bc.ceres.glayer.Layer;
import com.bc.ceres.glayer.LayerTypeRegistry;
import com.bc.ceres.grender.Rendering;
import com.bc.ceres.grender.Viewport;
import org.esa.snap.core.datamodel.*;
import org.esa.snap.core.util.ProductUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Daniel Knowles
 */

public class MetaDataLayer extends Layer {

    private static final MetaDataLayerType LAYER_TYPE = LayerTypeRegistry.getLayerType(MetaDataLayerType.class);

    private RasterDataNode raster;

    private ProductNodeHandler productNodeHandler;
    private MetaDataOnImage graticule;

    private double NULL_DOUBLE = -1.0;
    private double ptsToPixelsMultiplier = NULL_DOUBLE;



    public MetaDataLayer(RasterDataNode raster) {
        this(LAYER_TYPE, raster, initConfiguration(LAYER_TYPE.createLayerConfig(null), raster));
    }

    public MetaDataLayer(MetaDataLayerType type, RasterDataNode raster, PropertySet configuration) {
        super(type, configuration);
        setName("MetaData Layer");
        this.raster = raster;

        productNodeHandler = new ProductNodeHandler();
        raster.getProduct().addProductNodeListener(productNodeHandler);

        setTransparency(0.0);
    }

    private static PropertySet initConfiguration(PropertySet configurationTemplate, RasterDataNode raster) {
        configurationTemplate.setValue(MetaDataLayerType.PROPERTY_NAME_RASTER, raster);
        return configurationTemplate;
    }

    private Product getProduct() {
        return getRaster().getProduct();
    }

    RasterDataNode getRaster() {
        return raster;
    }

    @Override
    public void renderLayer(Rendering rendering) {

        getUserValues();

        String notes = "These are my notes";
        if (graticule == null) {
            final List<String> headerList = new ArrayList<String>();
            final List<String> header2List = new ArrayList<String>();
            final List<String> footerList = new ArrayList<String>();

            String header = getHeader();
            if (header != null && header.length() > 0) {
                String[] linesArray = header.split("(\\n|<br>)");
                for (String currentLine : linesArray) {
                    if (currentLine != null && currentLine.length() > 0) {
                        currentLine = replaceStringVariables(currentLine);
                        headerList.add(currentLine);
                    }
                }
            }

            String header2 = getHeader2();
            if (header2 != null && header2.length() > 0) {
                String[] linesArray = header2.split("(\\n|<br>)");
                for (String currentLine : linesArray) {
                    if (currentLine != null && currentLine.length() > 0) {
                        currentLine = replaceStringVariables(currentLine);
                        headerList.add(currentLine);
                    }
                }
            }

            String footer = getFooter();
            if (footer != null && footer.length() > 0) {
                String[] linesArray = footer.split("(\\n|<br>)");
                for (String currentLine : linesArray) {
                    if (currentLine != null && currentLine.length() > 0) {
                        currentLine = replaceStringVariables(currentLine);
                        footerList.add(currentLine);
                    }
                }
            }






            if (isIncludeFileName()) {
                footerList.add("File: " + raster.getProduct().getName());
            }

            if (isIncludeBandName()) {
                footerList.add("Band: " + raster.getName());
            }


            footerList.add("Band Description: " + raster.getDescription());
            footerList.add("Processing Version: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROCESSING_VERSION_KEYS));
            footerList.add("Sensor: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_SENSOR_KEYS));
            footerList.add("Platform: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PLATFORM_KEYS));
            footerList.add("Map Projection: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROJECTION_KEYS));
            footerList.add("Resolution: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_RESOLUTION_KEYS));
            footerList.add("ID: " + ProductUtils.getMetaData(raster.getProduct(), "id"));
            footerList.add("L2 Flag Names: " + ProductUtils.getMetaData(raster.getProduct(), "l2_flag_names"));
            footerList.add("Title: " + ProductUtils.getMetaData(raster.getProduct(), "title"));
            footerList.add("Product Type: " + raster.getProduct().getProductType());
            footerList.add("Scene Start Time: " + raster.getProduct().getStartTime());
            footerList.add("Scene End Time: " + raster.getProduct().getEndTime());
            footerList.add("Scene Size (w x h): " + raster.getRasterWidth() + " pixels x " + raster.getRasterHeight() + " pixels");
            footerList.add("Notes: " + notes);


            String footerMetadata = getFooterMetadata();
            if (footerMetadata != null && footerMetadata.length() > 0) {
                String[] paramsArray = footerMetadata.split("[ ,]+");
                for (String currentParam : paramsArray) {
                    if (currentParam != null && currentParam.length() > 0) {
                        currentParam = ProductUtils.getMetaData(raster.getProduct(), currentParam);
                        footerList.add(currentParam);
                    }
                }
            }



            //            TextGlyph[] textGlyphHeader = createTextGlyphsHeader(raster.getProduct().getName() + "processing_version=" + raster.getProduct().getMetadataRoot().getAttributeString("processing_version"));

            String[] attributes = raster.getProduct().getMetadataRoot().getAttributeNames();

            graticule = MetaDataOnImage.create(raster, headerList, header2List, footerList);
        }
        if (graticule != null) {

            final Graphics2D g2d = rendering.getGraphics();
            // added this to improve text
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            final Viewport vp = rendering.getViewport();
            final AffineTransform transformSave = g2d.getTransform();
            try {
                final AffineTransform transform = new AffineTransform();
                transform.concatenate(transformSave);
                transform.concatenate(vp.getModelToViewTransform());
                transform.concatenate(raster.getSourceImage().getModel().getImageToModelTransform(0));
                g2d.setTransform(transform);

                final MetaDataOnImage.TextGlyph[] textGlyphHeader = graticule.getTextGlyphsHeader();
                final MetaDataOnImage.TextGlyph[] textGlyphHeader2 = graticule.getTextGlyphsHeader2();
                final MetaDataOnImage.TextGlyph[] textGlyphsFooter = graticule.get_textGlyphsFooter();

                if (getHeaderShow()) {
                    drawTextHeaderFooter(g2d, textGlyphHeader, textGlyphHeader2, true, raster);
                }
                if (getFooterShow()) {
                    drawTextHeaderFooter(g2d, textGlyphsFooter, null, false, raster);
                }

            } finally {
                g2d.setTransform(transformSave);
            }
        }
    }


    private String replaceStringVariables(String header) {
        if (header != null && header.length() > 0) {
            header = header.replace("[FILE]",raster.getProduct().getName());
            header = header.replace("[BAND]",raster.getName());
        }

        return header;
    }

    private void getUserValues() {


    }


    private void drawTextHeaderFooter(Graphics2D g2d,
                                      final MetaDataOnImage.TextGlyph[] textGlyphs,
                                      final MetaDataOnImage.TextGlyph[] textGlyphs2,
                                      boolean isHeader,
                                      RasterDataNode raster) {


        Color origColor = (Color) g2d.getPaint();
        AffineTransform origTransform = g2d.getTransform();
        Font origFont = g2d.getFont();

        if (isHeader) {
            Font font = new Font(getLabelsFont(), getFontType(), getFontSizePixels());
            g2d.setFont(font);

            g2d.setPaint(getHeaderFontColor());


        } else {
           Font font = new Font(getLabelsFont(), getFontType(), getFooterFontSizePixels());
            g2d.setFont(font);
            g2d.setPaint(getFooterFontColor());


        }



//
//        Rectangle2D singleLetter = g2d.getFontMetrics().getStringBounds("W", g2d);
//        double letterWidth = singleLetter.getWidth();

        double heightInformationBlock = 0.0;
        double maxWidthInformationBlock = 0.0;


        for (MetaDataOnImage.TextGlyph glyph : textGlyphs) {
            Rectangle2D labelBounds = g2d.getFontMetrics().getStringBounds(glyph.getText(), g2d);
            maxWidthInformationBlock = Math.max(labelBounds.getWidth(), maxWidthInformationBlock);
            heightInformationBlock += labelBounds.getHeight();
        }


        double yTopTranslateFirstLine;
        double yBottomTranslateFirstLine;

        if (isHeader) {
            yTopTranslateFirstLine = -heightInformationBlock - raster.getRasterHeight() * (getHeaderGapFactor() / 100);
            yBottomTranslateFirstLine =  raster.getRasterHeight() * (getHeaderGapFactor() / 100);
        } else {
            yTopTranslateFirstLine = -heightInformationBlock - raster.getRasterHeight() * (getFooterGapFactor() / 100);
            yBottomTranslateFirstLine =  raster.getRasterHeight() * (getFooterGapFactor() / 100);
        }

        for (MetaDataOnImage.TextGlyph glyph : textGlyphs) {

            g2d.translate(glyph.getX(), glyph.getY());

            g2d.rotate(glyph.getAngle());

            double rotation = 90.0;
            double theta = (rotation / 180) * Math.PI;
            g2d.rotate(-1 * Math.PI + theta);

            Rectangle2D labelBounds = g2d.getFontMetrics().getStringBounds(glyph.getText(), g2d);

            String location = (isHeader) ? getLocationHeader() : getLocationFooter();

            float  xOffset = 0;
            float  yOffset = 0;
            switch (location) {

                case MetaDataLayerType.LOCATION_TOP_LEFT:
                    xOffset = 0;
                    yOffset = 0 + (float) yTopTranslateFirstLine;
                    break;

                case MetaDataLayerType.LOCATION_TOP_CENTER_JUSTIFY_LEFT:
                    xOffset = (float) (-(maxWidthInformationBlock / 2.0) + (raster.getRasterWidth() / 2.0));
                    yOffset = 0 + (float) yTopTranslateFirstLine;
                    break;

                case MetaDataLayerType.LOCATION_TOP_CENTER:
                    xOffset = (float) (-(labelBounds.getWidth() / 2.0) + (raster.getRasterWidth() / 2.0));
                    yOffset = 0 + (float) yTopTranslateFirstLine;
                    break;

                case MetaDataLayerType.LOCATION_TOP_RIGHT:
                    xOffset = (float) (raster.getRasterWidth() - maxWidthInformationBlock);
                    yOffset = 0 + (float) yTopTranslateFirstLine;
                    break;

                case MetaDataLayerType.LOCATION_BOTTOM_LEFT:
                    xOffset = 0;
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() + yBottomTranslateFirstLine);
                    break;

                case MetaDataLayerType.LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT:
                    xOffset = (float) (-(maxWidthInformationBlock / 2.0) + (raster.getRasterWidth() / 2.0));
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() + yBottomTranslateFirstLine);
                    break;

                case MetaDataLayerType.LOCATION_BOTTOM_CENTER:
                    xOffset = (float) (-(labelBounds.getWidth() / 2.0) + (raster.getRasterWidth() / 2.0));
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() + yBottomTranslateFirstLine);
                    break;

                case MetaDataLayerType.LOCATION_BOTTOM_RIGHT:
                    xOffset = (float) (raster.getRasterWidth() - maxWidthInformationBlock);
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() + yBottomTranslateFirstLine);
                    break;

//                default:
//                    xOffset = 0;
//                    yOffset = 0;
            }



            float xMod = (float) (Math.cos(theta));
            float yMod = -1 * (float) (Math.sin(theta));

            g2d.drawString(glyph.getText(),  xMod + xOffset,  yMod + yOffset);

            g2d.rotate(1 * Math.PI - theta);
            g2d.rotate(-glyph.getAngle());
//            g2d.translate(-glyph.getX(), -glyph.getY());

            g2d.translate(0,labelBounds.getHeight());
        }
        g2d.setTransform(origTransform);

        g2d.setPaint(origColor);
        g2d.setFont(origFont);
    }






    private AlphaComposite getAlphaComposite(double itemTransparancy) {
        double combinedAlpha = (1.0 - getTransparency()) * (1.0 - itemTransparancy);
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) combinedAlpha);
    }

    @Override
    public void disposeLayer() {
        final Product product = getProduct();
        if (product != null) {
            product.removeProductNodeListener(productNodeHandler);
            graticule = null;
            raster = null;
        }
    }

    @Override
    protected void fireLayerPropertyChanged(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (
                propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER2_TEXTFIELD_KEY) ||

                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_TEXTFIELD_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FILE_NAME_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_BAND_NAME_KEY) ||

                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_LOCATION_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_GAP_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_FONT_SIZE_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_FONT_COLOR_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_FONT_STYLE_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_FONT_ITALIC_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_FONT_BOLD_KEY) ||

                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_LOCATION_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_GAP_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_SIZE_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_COLOR_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_STYLE_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_ITALIC_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_BOLD_KEY)
        ) {
            graticule = null;
        }
        if (getConfiguration().getProperty(propertyName) != null) {
            getConfiguration().setValue(propertyName, event.getNewValue());
        }
        super.fireLayerPropertyChanged(event);
    }



    private String getHeader() {
        String header = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD_KEY,
                MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD_DEFAULT);
        return header;
    }

    private String getHeader2() {
        String header2 = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER2_TEXTFIELD_KEY,
                MetaDataLayerType.PROPERTY_HEADER2_TEXTFIELD_DEFAULT);
        return header2;
    }

    private boolean getHeaderShow() {
        boolean header = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_SHOW_KEY,
                MetaDataLayerType.PROPERTY_HEADER_SHOW_DEFAULT);
        return header;
    }

    private String getFooter() {
        String footer = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_TEXTFIELD_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_TEXTFIELD_DEFAULT);
        return footer;
    }

    private boolean getFooterShow() {
        boolean footer = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_SHOW_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_SHOW_DEFAULT);
        return footer;
    }

    private String getFooterMetadata() {
        String footerMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA_DEFAULT);
        return footerMetadata;
    }


    private boolean isIncludeFileName() {
        boolean isIncludeFileName = getConfigurationProperty(MetaDataLayerType.PROPERTY_FILE_NAME_KEY,
                MetaDataLayerType.PROPERTY_FILE_NAME_DEFAULT);
        return isIncludeFileName;
    }

    private boolean isIncludeBandName() {
        boolean isIncludeBandName = getConfigurationProperty(MetaDataLayerType.PROPERTY_BAND_NAME_KEY,
                MetaDataLayerType.PROPERTY_BAND_NAME_DEFAULT);
        return isIncludeBandName;
    }


    private double getFooterGapFactor() {
        double locationGapFactor = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_GAP_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_GAP_DEFAULT);
        return locationGapFactor;
    }

    private double getHeaderGapFactor() {
        double headerGapFactor = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_GAP_KEY,
                MetaDataLayerType.PROPERTY_HEADER_GAP_DEFAULT);
        return headerGapFactor;
    }


    private String getLocationHeader() {
        String location = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_LOCATION_KEY,
                MetaDataLayerType.PROPERTY_HEADER_LOCATION_DEFAULT);
        return location;
    }

    private String getLocationFooter() {
        String footerLocation = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_LOCATION_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_LOCATION_DEFAULT);
        return footerLocation;
    }











    private int getFontSizePixels() {
        int fontSizePts = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_SIZE_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_SIZE_DEFAULT);

        return (int) Math.round(getPtsToPixelsMultiplier() * fontSizePts);
    }

    private int getFooterFontSizePixels() {
        int fontSizePts = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_FONT_SIZE_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_FONT_SIZE_DEFAULT);

        return (int) Math.round(getPtsToPixelsMultiplier() * fontSizePts);
    }

    private Color getHeaderFontColor() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_COLOR_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_COLOR_DEFAULT);
    }

    private Color getFooterFontColor() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_FONT_COLOR_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_FONT_COLOR_DEFAULT);
    }

    private double getPtsToPixelsMultiplier() {

        if (ptsToPixelsMultiplier == NULL_DOUBLE) {
            double maxSideSize = Math.max(raster.getRasterHeight(), raster.getRasterWidth());

            ptsToPixelsMultiplier = maxSideSize * 0.001;
        }


        return ptsToPixelsMultiplier;
    }


    private String getLabelsFont() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_STYLE_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_STYLE_DEFAULT);
    }

    private Boolean isLabelsItalic() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_ITALIC_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_ITALIC_DEFAULT);
    }

    private Boolean isLabelsBold() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_BOLD_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_BOLD_DEFAULT);
    }

    private int getFontType() {
        if (isLabelsItalic() && isLabelsBold()) {
            return Font.ITALIC | Font.BOLD;
        } else if (isLabelsItalic()) {
            return Font.ITALIC;
        } else if (isLabelsBold()) {
            return Font.BOLD;
        } else {
            return Font.PLAIN;
        }
    }







    private class ProductNodeHandler extends ProductNodeListenerAdapter {

        /**
         * Overwrite this method if you want to be notified when a node changed.
         *
         * @param event the product node which the listener to be notified
         */
        @Override
        public void nodeChanged(ProductNodeEvent event) {
            if (event.getSourceNode() == getProduct() && Product.PROPERTY_NAME_SCENE_GEO_CODING.equals(
                    event.getPropertyName())) {
                // Force recreation
                graticule = null;
                fireLayerDataChanged(getModelBounds());
            }
        }
    }

}
