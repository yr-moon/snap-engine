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
    private MetaDataOnImage headerFooter;

    private double NULL_DOUBLE = -1.0;
    private double ptsToPixelsMultiplier = NULL_DOUBLE;


    public MetaDataLayer(RasterDataNode raster) {
        this(LAYER_TYPE, raster, initConfiguration(LAYER_TYPE.createLayerConfig(null), raster));
    }

    public MetaDataLayer(MetaDataLayerType type, RasterDataNode raster, PropertySet configuration) {
        super(type, configuration);
        setName("Title Metadata Layer");
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

        if (headerFooter == null) {
            final List<String> headerList = new ArrayList<String>();
            final List<String> footerList = new ArrayList<String>();

//            String header = getHeader();
//            if (header != null && header.length() > 0) {
//                String[] linesArray = header.split("(\\n|<br>)");
//                for (String currentLine : linesArray) {
//                    if (currentLine != null && currentLine.length() > 0) {
////                        currentLine = replaceStringVariables(currentLine, getHeaderMetadataKeysShow());
//                        currentLine = replaceStringVariables(currentLine, false);
//                        headerList.add(currentLine);
//                    }
//                }
//            }
//
//            String header2 = getHeader2();
//            if (header2 != null && header2.length() > 0) {
//                String[] linesArray = header2.split("(\\n|<br>)");
//                for (String currentLine : linesArray) {
//                    if (currentLine != null && currentLine.length() > 0) {
//                        currentLine = replaceStringVariables(currentLine, false);
////                        currentLine = replaceStringVariables(currentLine, getHeaderMetadataKeysShow());
//                        headerList.add(currentLine);
//                    }
//                }
//            }

            for (String curr : getHeaderFooterLinesArray(getHeader())) {
                headerList.add(curr);
            }

            for (String curr : getHeaderFooterLinesArray(getHeader2())) {
                headerList.add(curr);
            }

            for (String curr : getHeaderFooterLinesArray(getHeader3())) {
                headerList.add(curr);
            }

            String headerMetadata = getHeaderMetadata();
            if (headerMetadata != null && headerMetadata.length() > 0) {
                String[] paramsArray = headerMetadata.split("[ ,]+");
                for (String currentParam : paramsArray) {
                    currentParam = currentParam.trim();
                    if (currentParam != null && currentParam.length() > 0) {
                        if (currentParam.startsWith("[")) {
                            String key = currentParam;
                            int length = key.length();
                            key = key.substring(1, length - 1);
                            currentParam = replaceStringVariables(currentParam.toUpperCase(), getHeaderMetadataKeysShow());

                            if (getHeaderMetadataKeysShow()) {
                                currentParam = key + getFooterMetadataDelimiter() + currentParam;
                            }
                        } else {
                            if (getHeaderMetadataKeysShow()) {
                                currentParam = currentParam + getFooterMetadataDelimiter() + ProductUtils.getMetaData(raster.getProduct(), currentParam);
                            } else {
                                currentParam = ProductUtils.getMetaData(raster.getProduct(), currentParam);
                            }
                        }
                        headerList.add(currentParam);
                    }
                }
            }


//            String footer = getFooter();
//            if (footer != null && footer.length() > 0) {
//                String[] linesArray = footer.split("(\\n|<br>)");
//                for (String currentLine : linesArray) {
//                    if (currentLine != null && currentLine.length() > 0) {
//                        currentLine = replaceStringVariables(currentLine, getFooterMetadataKeysShow());
//                        footerList.add(currentLine);
//                    }
//                }
//            }

            for (String curr : getHeaderFooterLinesArray(getFooter())) {
                footerList.add(curr);
            }





//            footerList.add("Band Description: " + raster.getDescription());
//            footerList.add("Processing Version: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROCESSING_VERSION_KEYS));
//            footerList.add("Sensor: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_SENSOR_KEYS));
//            footerList.add("Platform: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PLATFORM_KEYS));
//            footerList.add("Map Projection: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROJECTION_KEYS));
//            footerList.add("Resolution: " + ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_RESOLUTION_KEYS));
//            footerList.add("ID: " + ProductUtils.getMetaData(raster.getProduct(), "id"));
//            footerList.add("L2 Flag Names: " + ProductUtils.getMetaData(raster.getProduct(), "l2_flag_names"));
//            footerList.add("Title: " + ProductUtils.getMetaData(raster.getProduct(), "title"));
//            footerList.add("Product Type: " + raster.getProduct().getProductType());
//            footerList.add("Scene Start Time: " + raster.getProduct().getStartTime());
//            footerList.add("Scene End Time: " + raster.getProduct().getEndTime());
//            footerList.add("Scene Size (w x h): " + raster.getRasterWidth() + " pixels x " + raster.getRasterHeight() + " pixels");


            ArrayList<String> footerMetadataCombinedArrayList = new ArrayList<String>();

            for (String curr : getMetadataArrayList(getFooterMetadata())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata2())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata3())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata4())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata5())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            if (displayAllMetadata()) {
                String[] allAttributes = getProduct().getMetadataRoot().getElement("Global_Attributes").getAttributeNames();
                for (String curr : allAttributes) {
                    footerMetadataCombinedArrayList.add(curr);
                }
            }

            for (String currKey : footerMetadataCombinedArrayList) {
                if (currKey != null && currKey.length() > 0) {
                    String currParam = null;
                    if (currKey.startsWith("[")) {
                        int length = currKey.length();
                        if (length > 2) {
                            currParam = replaceStringVariables(currKey.toUpperCase(), false);

                            if (getFooterMetadataKeysShow()) {
                                String keyTrimmed = currKey.substring(1, length - 1);
                                currParam = keyTrimmed + getFooterMetadataDelimiter() + currParam;
                            }
                        }
                    } else {
                        String key = currKey;
                        currParam = ProductUtils.getMetaData(raster.getProduct(), currKey);

                        if (getFooterMetadataKeysShow()) {
                            currParam = key + getFooterMetadataDelimiter() + currParam;
                        }
                    }
                    if (currParam != null && currParam.trim() != null && currParam.length() > 0) {
                        footerList.add(currParam);
//                    } else {
//                        footerList.add(currKey);
                    }
                }
            }


            //            TextGlyph[] textGlyphHeader = createTextGlyphsHeader(raster.getProduct().getName() + "processing_version=" + raster.getProduct().getMetadataRoot().getAttributeString("processing_version"));

            String[] attributes = raster.getProduct().getMetadataRoot().getAttributeNames();

            headerFooter = MetaDataOnImage.create(raster, headerList, footerList);
        }
        if (headerFooter != null) {

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

                final MetaDataOnImage.TextGlyph[] textGlyphHeader = headerFooter.getTextGlyphsHeader();
                final MetaDataOnImage.TextGlyph[] textGlyphsFooter = headerFooter.get_textGlyphsFooter();

                if (getHeaderShow()) {
                    drawTextHeaderFooter(g2d, textGlyphHeader, true, raster);
                }
                if (getFooterShow()) {
                    drawTextHeaderFooter(g2d, textGlyphsFooter, false, raster);
                }

            } finally {
                g2d.setTransform(transformSave);
            }
        }
    }

    private String replaceStringVariablesCase(String inputString, String key, String replacement) {
        if (inputString != null && inputString.length() > 0 && key != null && key.length() > 0 && replacement != null) {
            inputString = inputString.replace(key, replacement);
            inputString = inputString.replace(key.toLowerCase(), replacement);
            inputString = inputString.replace(key.toLowerCase(), replacement);
            String keyTitleCase = convertToTitleCase(key);
            inputString = inputString.replace(keyTitleCase.toLowerCase(), replacement);
        }

        return inputString;
    }


    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    private String replaceStringVariables(String inputString, boolean showKeys) {
        if (inputString != null && inputString.length() > 0) {
//            inputString = inputString.replace("[FILE]", raster.getProduct().getName());
//            inputString = inputString.replace("[File]", raster.getProduct().getName());
            inputString = replaceStringVariablesCase(inputString, "[FILE]", raster.getProduct().getName());

            inputString = inputString.replace("[BAND]", raster.getName());
            inputString = inputString.replace("[BAND_DESCRIPTION]", raster.getDescription());

            inputString = inputString.replace("[PROCESSING_VERSION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROCESSING_VERSION_KEYS));
            inputString = inputString.replace("[SENSOR]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_SENSOR_KEYS));
            inputString = inputString.replace("[PLATFORM]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PLATFORM_KEYS));
            inputString = inputString.replace("[PROJECTION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROJECTION_KEYS));
            inputString = inputString.replace("[RESOLUTION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_RESOLUTION_KEYS));

            inputString = inputString.replace("[DAY_NIGHT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_DAY_NIGHT_KEYS));
            inputString = inputString.replace("[ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_ORBIT_KEYS));
            inputString = inputString.replace("[START_ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_START_ORBIT_KEYS));
            inputString = inputString.replace("[END_ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_END_ORBIT_KEYS));


            inputString = inputString.replace("[ID]", ProductUtils.getMetaData(raster.getProduct(), "id"));
            inputString = inputString.replace("[L2_FLAG_NAMES]", ProductUtils.getMetaData(raster.getProduct(), "l2_flag_names"));

            inputString = inputString.replace("[TITLE]", raster.getProduct().toString());
            inputString = inputString.replace("[FILE_LOCATION]", raster.getProduct().getFileLocation().toString());
            inputString = inputString.replace("[PRODUCT_TYPE]", raster.getProduct().getProductType());
            inputString = inputString.replace("[SCENE_START_TIME]", raster.getProduct().getStartTime().toString());
            inputString = inputString.replace("[SCENE_END_TIME]", raster.getProduct().getEndTime().toString());
            inputString = inputString.replace("[SCENE_HEIGHT]", Integer.toString(raster.getRasterHeight()));
            inputString = inputString.replace("[SCENE_WIDTH]", Integer.toString(raster.getRasterWidth()));
            String sceneSize = "(w x h) " + raster.getRasterWidth() + " pixels x " + raster.getRasterHeight() + " pixels";
            inputString = inputString.replace("[SCENE_SIZE]", sceneSize);


            String metaId = null;
            String beforeMetaData = "";
            String afterMetaData = "";
            String metaStart = "";

//            String META_START = "<META>";
//            String META_END = "</META>";

            String META_START = "<META=";
            String META_END = ">";

            inputString = inputString.replace("[META]", META_START);
            inputString = inputString.replace("[meta]", META_START);
            inputString = inputString.replace("<meta>", META_START);
            inputString = inputString.replace("[/META]", META_END);
            inputString = inputString.replace("[/meta]", META_END);
            inputString = inputString.replace("</meta>", META_END);


            int whileCnt = 0;
            boolean hasMetaData = (inputString.contains(META_START) && inputString.contains(META_START)) ? true : false;


            while (hasMetaData && whileCnt < 10) {
                String[] arr1 = inputString.split(META_START, 2);

                if (arr1 != null) {
                    if (arr1.length == 1) {
                        beforeMetaData = arr1[0];
                        metaStart = "";
                    } else if (arr1.length == 2) {
                        beforeMetaData = arr1[0];
                        metaStart = arr1[1];
                    }
                } else {
                    beforeMetaData = "";
                    metaStart = "";
                }

                if (metaStart != null && metaStart.length() > 0) {
                    String[] arr2 = metaStart.split(META_END, 2);

                    if (arr2 != null && arr2.length == 2) {
                        metaId = arr2[0];
                        afterMetaData = arr2[1];
                    }
                }

                if (metaId != null && metaId.length() > 0) {
                    if (showKeys) {
                        inputString = beforeMetaData + metaId + getFooterMetadataDelimiter() + ProductUtils.getMetaData(raster.getProduct(), metaId) + afterMetaData;
                    } else {
                        inputString = beforeMetaData + ProductUtils.getMetaData(raster.getProduct(), metaId) + afterMetaData;
                    }
                }

                hasMetaData = (inputString.contains(META_START) && inputString.contains(META_END)) ? true : false;

                whileCnt++;
            }

        }

        return inputString;
    }


    private void getUserValues() {


    }


    private void drawTextHeaderFooter(Graphics2D g2d,
                                      final MetaDataOnImage.TextGlyph[] textGlyphs,
                                      boolean isHeader,
                                      RasterDataNode raster) {


        Color origColor = (Color) g2d.getPaint();
        AffineTransform origTransform = g2d.getTransform();
        Font origFont = g2d.getFont();

        if (isHeader) {
            Font font = new Font(getHeaderFontStyle(), getHeaderFontType(), getHeaderFontSizePixels());
            g2d.setFont(font);

            g2d.setPaint(getHeaderFontColor());


        } else {
            Font font = new Font(getFooterFontStyle(), getFooterFontType(), getFooterFontSizePixels());
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
            yBottomTranslateFirstLine = raster.getRasterHeight() * (getHeaderGapFactor() / 100);
        } else {
            yTopTranslateFirstLine = -heightInformationBlock - raster.getRasterHeight() * (getFooterGapFactor() / 100);
            yBottomTranslateFirstLine = raster.getRasterHeight() * (getFooterGapFactor() / 100);
        }

        for (MetaDataOnImage.TextGlyph glyph : textGlyphs) {

            g2d.translate(glyph.getX(), glyph.getY());

            g2d.rotate(glyph.getAngle());

            double rotation = 90.0;
            double theta = (rotation / 180) * Math.PI;
            g2d.rotate(-1 * Math.PI + theta);

            Rectangle2D labelBounds = g2d.getFontMetrics().getStringBounds(glyph.getText(), g2d);

            String location = (isHeader) ? getLocationHeader() : getLocationFooter();

            float xOffset = 0;
            float yOffset = 0;
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

                case MetaDataLayerType.LOCATION_RIGHT:
                    xOffset = (float) (raster.getRasterWidth() + raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = 0;
                    break;

                case MetaDataLayerType.LOCATION_LEFT:
                    xOffset = (float) (-maxWidthInformationBlock - raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = 0;
                    break;

//                default:
//                    xOffset = 0;
//                    yOffset = 0;
            }


            float xMod = (float) (Math.cos(theta));
            float yMod = -1 * (float) (Math.sin(theta));

            g2d.drawString(glyph.getText(), xMod + xOffset, yMod + yOffset);

            g2d.rotate(1 * Math.PI - theta);
            g2d.rotate(-glyph.getAngle());
//            g2d.translate(-glyph.getX(), -glyph.getY());

            g2d.translate(0, labelBounds.getHeight());
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
            headerFooter = null;
            raster = null;
        }
    }

    @Override
    protected void fireLayerPropertyChanged(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (
                propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD2_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD3_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_METADATA_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_HEADER_METADATA_KEYS_SHOW_KEY) ||

                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_TEXTFIELD_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA2_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA3_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA4_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA5_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEYS_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_METADATA_DELIMITER_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_ALL_METADATA_SHOW_KEY) ||


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
            headerFooter = null;
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
        String header2 = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD2_KEY,
                MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD2_DEFAULT);
        return header2;
    }

    private String getHeader3() {
        String header3 = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD3_KEY,
                MetaDataLayerType.PROPERTY_HEADER_TEXTFIELD3_DEFAULT);
        return header3;
    }

    private boolean getHeaderShow() {
        boolean header = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_SHOW_KEY,
                MetaDataLayerType.PROPERTY_HEADER_SHOW_DEFAULT);
        return header;
    }

    private boolean getHeaderMetadataKeysShow() {
        boolean headerMetadataKeysShow = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_METADATA_KEYS_SHOW_KEY,
                MetaDataLayerType.PROPERTY_HEADER_METADATA_KEYS_SHOW_DEFAULT);
        return headerMetadataKeysShow;
    }

    private boolean getFooterMetadataKeysShow() {
        boolean footerMetadataKeysShow = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEYS_SHOW_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA_KEYS_SHOW_DEFAULT);
        return footerMetadataKeysShow;
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

    private String getFooterMetadata2() {
        String footerMetadata2 = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA2_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA2_DEFAULT);
        return footerMetadata2;
    }

    private String getFooterMetadata3() {
        String footerMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA3_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA3_DEFAULT);
        return footerMetadata;
    }

    private String getFooterMetadata4() {
        String footerMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA4_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA4_DEFAULT);
        return footerMetadata;
    }

    private String getFooterMetadata5() {
        String footerMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA5_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA5_DEFAULT);
        return footerMetadata;
    }


    private String getFooterMetadataDelimiter() {
        String footerMetadataDelimiter = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_METADATA_DELIMITER_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_METADATA_DELIMITER_DEFAULT);
        return footerMetadataDelimiter;
    }



    private ArrayList<String> getFooterMetadataArrayList() {

        ArrayList<String> footerMetadataArrayList = new ArrayList<String>();
        String footerMetadata = getFooterMetadata();
        if (footerMetadata != null && footerMetadata.trim() != null && footerMetadata.trim().length() > 0) {
            String[] paramsArray = footerMetadata.split("[ ,]+");
            for (String currentParam : paramsArray) {
                if (currentParam != null && currentParam.trim() != null && currentParam.trim().length() > 0) {
                    footerMetadataArrayList.add(currentParam.trim());
                }
            }
        }

//        return (String[]) footerMetadataArrayList.toArray();
        return footerMetadataArrayList;
    }

    private ArrayList<String> getMetadataArrayList(String metadataList) {

        ArrayList<String> footerMetadataArrayList = new ArrayList<String>();
        if (metadataList != null && metadataList.trim() != null && metadataList.trim().length() > 0) {
            String[] paramsArray = metadataList.split("[ ,]+");
            for (String currentParam : paramsArray) {
                if (currentParam != null && currentParam.trim() != null && currentParam.trim().length() > 0) {
                    footerMetadataArrayList.add(currentParam.trim());
                }
            }
        }

        return footerMetadataArrayList;
    }


    private ArrayList<String> getHeaderFooterLinesArray(String text) {
        ArrayList<String> lineArrayList = new ArrayList<String>();

        if (text != null && text.length() > 0) {
            String[] linesArray = text.split("(\\n|<br>)");
            for (String currentLine : linesArray) {
                if (currentLine != null && currentLine.length() > 0) {
                    currentLine = replaceStringVariables(currentLine, false);
                    lineArrayList.add(currentLine);
                }
            }
        }

        return lineArrayList;
    }


    private String getHeaderMetadata() {
        String headerMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_METADATA_KEY,
                MetaDataLayerType.PROPERTY_HEADER_METADATA_DEFAULT);
        return headerMetadata;
    }

    private boolean displayAllMetadata() {
        boolean displayAllMetadata = getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_ALL_METADATA_SHOW_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_ALL_METADATA_SHOW_DEFAULT);
        return displayAllMetadata;
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


    private int getHeaderFontSizePixels() {
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


    private String getHeaderFontStyle() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_STYLE_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_STYLE_DEFAULT);
    }

    private String getFooterFontStyle() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_FONT_STYLE_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_FONT_STYLE_DEFAULT);
    }

    private Boolean isHeaderFontItalic() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_ITALIC_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_ITALIC_DEFAULT);
    }

    private Boolean isHeaderFontBold() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_HEADER_FONT_BOLD_KEY,
                MetaDataLayerType.PROPERTY_HEADER_FONT_BOLD_DEFAULT);
    }

    private int getHeaderFontType() {
        if (isHeaderFontItalic() && isHeaderFontBold()) {
            return Font.ITALIC | Font.BOLD;
        } else if (isHeaderFontItalic()) {
            return Font.ITALIC;
        } else if (isHeaderFontBold()) {
            return Font.BOLD;
        } else {
            return Font.PLAIN;
        }
    }


    private Boolean isFooterFontItalic() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_FONT_ITALIC_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_FONT_ITALIC_DEFAULT);
    }

    private Boolean isFooterFontBold() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER_FONT_BOLD_KEY,
                MetaDataLayerType.PROPERTY_FOOTER_FONT_BOLD_DEFAULT);
    }

    private int getFooterFontType() {
        if (isFooterFontItalic() && isFooterFontBold()) {
            return Font.ITALIC | Font.BOLD;
        } else if (isFooterFontItalic()) {
            return Font.ITALIC;
        } else if (isFooterFontBold()) {
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
                headerFooter = null;
                fireLayerDataChanged(getModelBounds());
            }
        }
    }

}
