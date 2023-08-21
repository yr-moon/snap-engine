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
            final List<String> footer2List = new ArrayList<String>();


            for (String curr : getHeaderFooterLinesArray(getHeader())) {
                headerList.add(curr);
            }

            for (String curr : getHeaderFooterLinesArray(getHeader2())) {
                headerList.add(curr);
            }

            for (String curr : getHeaderFooterLinesArray(getHeader3())) {
                headerList.add(curr);
            }

            ArrayList<String> headerMetadataCombinedArrayList = new ArrayList<String>();

            for (String curr : getMetadataArrayList(getHeaderMetadata())) {
                headerMetadataCombinedArrayList.add(curr);
            }

            addFromMetadataList(headerMetadataCombinedArrayList, headerList, true, true);


            for (String curr : getHeaderFooterLinesArray(getFooter())) {
                footerList.add(curr);
            }



            ArrayList<String> footerMetadataCombinedArrayList = new ArrayList<String>();
            ArrayList<String> footerBandMetadataCombinedArrayList = new ArrayList<String>();
            ArrayList<String> footerInfoCombinedArrayList = new ArrayList<String>();

            for (String curr : getMetadataArrayList(getFooterMetadata())) {
                footerInfoCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata2())) {
                footerInfoCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata3())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata4())) {
                footerMetadataCombinedArrayList.add(curr);
            }

            for (String curr : getMetadataArrayList(getFooterMetadata5())) {
                footerBandMetadataCombinedArrayList.add(curr);
            }

            if (displayAllMetadata()) {
                String[] allAttributes = getProduct().getMetadataRoot().getElement("Global_Attributes").getAttributeNames();
//                footerMetadataCombinedArrayList.add(" ");
//                footerMetadataCombinedArrayList.add("Global_Attributes");
                footerMetadataCombinedArrayList.clear();
                for (String curr : allAttributes) {
                    footerMetadataCombinedArrayList.add(curr);
                }
            }

            if (displayAllMetadata()) {
                String[] allAttributes = getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttributeNames();
//                footerMetadataCombinedArrayList.add(" ");
//                footerMetadataCombinedArrayList.add("Band_Attributes for: '" + raster.getName() + "'");
                footerBandMetadataCombinedArrayList.clear();
                for (String curr : allAttributes) {
                    footerBandMetadataCombinedArrayList.add(curr);
                }
            }

            footerList.add("");
            footerList.add("File-Band Info:");
            addFromMetadataList(footerInfoCombinedArrayList, footerList, false, false);

            footerList.add("");
            footerList.add("File Metadata: (Global_Attributes)");
            addFromMetadataList(footerMetadataCombinedArrayList, footerList, true, true);

            footerList.add("");
            footerList.add("Band Metadata (Band_Attributes):");
            addFromMetadataList(footerBandMetadataCombinedArrayList, footerList, true, false);


            for (String curr : getHeaderFooterLinesArray(getFooter2Textfield())) {
                footer2List.add(curr);
            }


            headerFooter = MetaDataOnImage.create(raster, headerList, footerList, footer2List);
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
                final MetaDataOnImage.TextGlyph[] textGlyphsFooter2 = headerFooter.get_textGlyphsFooter2();

                if (getHeaderShow()) {
                    drawTextHeaderFooter(g2d, textGlyphHeader, true, false, raster);
                }
                if (getFooterShow()) {
                    drawTextHeaderFooter(g2d, textGlyphsFooter, false, false, raster);
                }
                if (getFooter2Show()) {
                    drawTextHeaderFooter(g2d, textGlyphsFooter2, false, true, raster);
                }

            } finally {
                g2d.setTransform(transformSave);
            }
        }
    }

    private void addFromMetadataList(ArrayList<String> footerMetadataCombinedArrayList, List<String> footerList, boolean isMeta, boolean globalAttributes) {
        for (String currKey : footerMetadataCombinedArrayList) {
            if (currKey != null && currKey.length() > 0) {
                String currParam = null;
                if (!isMeta) {
                    int length = currKey.length();
                    if (length > 2) {
                        currParam = getDerivedMeta(currKey.toUpperCase());

                        if (getFooterMetadataKeysShow()) {
                            currParam = currKey + getFooterMetadataDelimiter() + currParam;
                        }
                    }
                } else {
                    String key = currKey;

                    if (globalAttributes) {
                        currParam = ProductUtils.getMetaData(raster.getProduct(), currKey);
                    } else {
                        currParam = ProductUtils.getBandMetaData(raster.getProduct(), currKey, raster.getName());
                    }

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

    private String replaceStringVariables(String inputString, boolean showKeys, String replaceKey) {
        if (inputString != null && inputString.length() > 0) {
////            inputString = inputString.replace("[FILE]", raster.getProduct().getName());
////            inputString = inputString.replace("[File]", raster.getProduct().getName());
//            inputString = replaceStringVariablesCase(inputString, "[FILE]", raster.getProduct().getName());
//
//            inputString = inputString.replace("[BAND]", raster.getName());
//            inputString = inputString.replace("[BAND_DESCRIPTION]", raster.getDescription());
//
//            inputString = inputString.replace("[PROCESSING_VERSION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROCESSING_VERSION_KEYS));
//            inputString = inputString.replace("[SENSOR]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_SENSOR_KEYS));
//            inputString = inputString.replace("[PLATFORM]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PLATFORM_KEYS));
//            inputString = inputString.replace("[PROJECTION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROJECTION_KEYS));
//            inputString = inputString.replace("[RESOLUTION]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_RESOLUTION_KEYS));
//
//            inputString = inputString.replace("[DAY_NIGHT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_DAY_NIGHT_KEYS));
//            inputString = inputString.replace("[ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_ORBIT_KEYS));
//            inputString = inputString.replace("[START_ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_START_ORBIT_KEYS));
//            inputString = inputString.replace("[END_ORBIT]", ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_END_ORBIT_KEYS));
//
//
//            inputString = inputString.replace("[ID]", ProductUtils.getMetaData(raster.getProduct(), "id"));
//            inputString = inputString.replace("[L2_FLAG_NAMES]", ProductUtils.getMetaData(raster.getProduct(), "l2_flag_names"));
//
//            inputString = inputString.replace("[TITLE]", raster.getProduct().toString());
//            inputString = inputString.replace("[FILE_LOCATION]", raster.getProduct().getFileLocation().toString());
//            inputString = inputString.replace("[PRODUCT_TYPE]", raster.getProduct().getProductType());
//            inputString = inputString.replace("[SCENE_START_TIME]", raster.getProduct().getStartTime().toString());
//            inputString = inputString.replace("[SCENE_END_TIME]", raster.getProduct().getEndTime().toString());
//            inputString = inputString.replace("[SCENE_HEIGHT]", Integer.toString(raster.getRasterHeight()));
//            inputString = inputString.replace("[SCENE_WIDTH]", Integer.toString(raster.getRasterWidth()));
//            String sceneSize = "(w x h) " + raster.getRasterWidth() + " pixels x " + raster.getRasterHeight() + " pixels";
//            inputString = inputString.replace("[SCENE_SIZE]", sceneSize);
//
//            raster.getImageInfo().getColorPaletteDef().isLogScaled();
//            raster.getValidPixelExpression();
//            raster.getUnit();
//            raster.getOverlayMaskGroup().getNodeDisplayNames();
//            raster.getOverlayMaskGroup().getNodeNames();
//            raster.getProduct().getBand(raster.getName()).getSpectralWavelength();
//            raster.getProduct().getBand(raster.getName()).getAngularValue();
//            raster.getProduct().getBand(raster.getName()).getFlagCoding();
//            raster.getNoDataValue();
//            raster.isNoDataValueSet();
//            raster.isNoDataValueUsed();
//            raster.isScalingApplied();
//            raster.getScalingFactor();
//            raster.getScalingOffset();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("reference").getData().getElemString();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_min").getData().getElemString();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_max").getData().getElemString();



            String metaId = null;
            String beforeMetaData = "";
            String afterMetaData = "";
            String metaStart = "";


            String META_START = replaceKey;


            String META_END = ">";


            switch (META_START) {
                case "<META=":
                    inputString = inputString.replace("<Meta=", META_START);
                    inputString = inputString.replace("<meta=", META_START);
                    break;

                case "<FILE_META=":
                    inputString = inputString.replace("<File_Meta=", META_START);
                    inputString = inputString.replace("<file_meta=", META_START);
                    break;

                case "<BAND_META=":
                    inputString = inputString.replace("<Band_Meta=", META_START);
                    inputString = inputString.replace("<band_meta=", META_START);
                    break;

                case "<FILE_INFO=":
                    inputString = inputString.replace("<File_Info=", META_START);
                    inputString = inputString.replace("<file_info=", META_START);
                    break;

                case "<BAND_INFO=":
                    inputString = inputString.replace("<Band_Info=", META_START);
                    inputString = inputString.replace("<band_info=", META_START);
                    break;

                case "<INFO=":
                    inputString = inputString.replace("<Info=", META_START);
                    inputString = inputString.replace("<info=", META_START);
                    break;
            }





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
                    String value = "";


                        switch (META_START) {
                            case "<META=":
                                value = ProductUtils.getMetaData(raster.getProduct(), metaId);
                                break;

                            case "<FILE_META=":
                                value = ProductUtils.getMetaData(raster.getProduct(), metaId);
                                break;

                            case "<BAND_META=":
                                value = ProductUtils.getBandMetaData(raster.getProduct(), metaId, raster.getName());
                                break;

                            case "<FILE_INFO=":
                                value = getDerivedMeta(metaId);
                                break;

                            case "<BAND_INFO=":
                                value = getDerivedMeta(metaId);
                                break;

                            case "<INFO=":
                                value = getDerivedMeta(metaId);
                                break;
                        }

                    if (showKeys) {
                        inputString = beforeMetaData + metaId + getFooterMetadataDelimiter() +value + afterMetaData;
                    } else {
                        inputString = beforeMetaData + value + afterMetaData;
                    }
                }

                hasMetaData = (inputString.contains(META_START) && inputString.contains(META_END)) ? true : false;

                whileCnt++;
            }

        }

        return inputString;
    }

    private String getDerivedMeta(String inputString) {
        String value = "";

        if (inputString != null && inputString.length() > 0) {
            inputString = inputString.toUpperCase();

            switch (inputString) {

                case "FILE":
                    value = raster.getProduct().getName();
                    break;

                case "PROCESSING_VERSION":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROCESSING_VERSION_KEYS);
                    break;

                case "SENSOR":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_SENSOR_KEYS);
                    break;
                case "PLATFORM":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PLATFORM_KEYS);
                    break;
                case "PROJECTION":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_PROJECTION_KEYS);
                    break;
                case "RESOLUTION":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_RESOLUTION_KEYS);
                    break;

                case "DAY_NIGHT":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_DAY_NIGHT_KEYS);
                    break;
                case "ORBIT":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_ORBIT_KEYS);
                    break;
                case "START_ORBIT":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_START_ORBIT_KEYS);
                    break;
                case "END_ORBIT":
                    value = ProductUtils.getMetaData(raster.getProduct(), ProductUtils.METADATA_POSSIBLE_END_ORBIT_KEYS);
                    break;

                case "BAND":
                    value = raster.getName();
                    break;

                case "BAND_DESCRIPTION":
                    value = raster.getDescription();
                    break;

                case "FILE_LOCATION":
                    value = raster.getProduct().getFileLocation().toString();
                    break;

                case "PRODUCT_TYPE":
                    value = raster.getProduct().getProductType();
                    break;

                case "SCENE_START_TIME":
                    value = raster.getProduct().getStartTime().toString();
                    break;

                case "SCENE_END_TIME":
                    value = raster.getProduct().getEndTime().toString();
                    break;

                case "SCENE_HEIGHT":
                    value = Integer.toString(raster.getRasterHeight());
                    break;

                case "SCENE_WIDTH":
                    value = Integer.toString(raster.getRasterWidth());
                    break;

                case "SCENE_SIZE":
                    value = "(w x h) " + raster.getRasterWidth() + " pixels x " + raster.getRasterHeight() + " pixels";
                    break;

                case "MY_INFO":
                    value = getMyInfo();
                    break;

                case "MY_INFO1":
                    value = getMyInfo1();
                    break;

                case "MY_INFO2":
                    value = getMyInfo2();
                    break;

                case "MY_INFO3":
                    value = getMyInfo3();
                    break;

                case "[MY_INFO4]":
                    value = getMyInfo4();
                    break;
            }

            //
//            raster.getImageInfo().getColorPaletteDef().isLogScaled();
//            raster.getValidPixelExpression();
//            raster.getUnit();
//            raster.getOverlayMaskGroup().getNodeDisplayNames();
//            raster.getOverlayMaskGroup().getNodeNames();
//            raster.getProduct().getBand(raster.getName()).getSpectralWavelength();
//            raster.getProduct().getBand(raster.getName()).getAngularValue();
//            raster.getProduct().getBand(raster.getName()).getFlagCoding();
//            raster.getNoDataValue();
//            raster.isNoDataValueSet();
//            raster.isNoDataValueUsed();
//            raster.isScalingApplied();
//            raster.getScalingFactor();
//            raster.getScalingOffset();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("reference").getData().getElemString();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_min").getData().getElemString();
//            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_max").getData().getElemString();

        }




//            inputString = inputString.replace("[TITLE]", raster.getProduct().toString());



            raster.getImageInfo().getColorPaletteDef().isLogScaled();
        raster.isLog10Scaled();
        raster.getValidPixelExpression();
            raster.getUnit();
            raster.getOverlayMaskGroup().getNodeDisplayNames();
            raster.getOverlayMaskGroup().getNodeNames();
            raster.getProduct().getBand(raster.getName()).getSpectralWavelength();
            raster.getProduct().getBand(raster.getName()).getAngularValue();
            raster.getProduct().getBand(raster.getName()).getFlagCoding();
            raster.getNoDataValue();
            raster.isNoDataValueSet();
            raster.isNoDataValueUsed();
            raster.isScalingApplied();
            raster.getScalingFactor();
            raster.getScalingOffset();
            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("reference").getData().getElemString();
            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_min").getData().getElemString();
            raster.getProduct().getMetadataRoot().getElement("Band_Attributes").getElement(raster.getName()).getAttribute("valid_max").getData().getElemString();



        return value;
    }

    private void getUserValues() {


    }


    private void drawTextHeaderFooter(Graphics2D g2d,
                                      final MetaDataOnImage.TextGlyph[] textGlyphs,
                                      boolean isHeader,
                                      boolean isFooter2,
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

            String location;
            if (isHeader) {
                location = getLocationHeader();
            } else if (isFooter2) {
                location = getLocationFooter2();
            } else {
                location = getLocationFooter();
            }

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

                case MetaDataLayerType.LOCATION_RIGHT_CENTER:
                    xOffset = (float) (raster.getRasterWidth() + raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = (float) (raster.getRasterHeight()/2.0 + labelBounds.getHeight() - heightInformationBlock);
                    break;

                case MetaDataLayerType.LOCATION_RIGHT_BOTTOM:
                    xOffset = (float) (raster.getRasterWidth() + raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() - heightInformationBlock);
                    break;

                case MetaDataLayerType.LOCATION_LEFT:
                    xOffset = (float) (-maxWidthInformationBlock - raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = 0;
                    break;

                case MetaDataLayerType.LOCATION_LEFT_CENTER:
                    xOffset = (float) (-maxWidthInformationBlock - raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = (float) (raster.getRasterHeight()/2.0 + labelBounds.getHeight() - heightInformationBlock);
                    break;

                case MetaDataLayerType.LOCATION_LEFT_BOTTOM:
                    xOffset = (float) (-maxWidthInformationBlock - raster.getRasterWidth() * (getFooterGapFactor() / 100));;
                    yOffset = (float) (raster.getRasterHeight() + labelBounds.getHeight() - heightInformationBlock);
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

                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER2_SHOW_KEY) ||
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER2_TEXTFIELD_KEY) ||


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
                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER_FONT_BOLD_KEY) ||

                        propertyName.equals(MetaDataLayerType.PROPERTY_FOOTER2_LOCATION_KEY
                        )
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



    private boolean getFooter2Show() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER2_SHOW_KEY,
                MetaDataLayerType.PROPERTY_FOOTER2_SHOW_DEFAULT);
    }

    private String getFooter2Textfield() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER2_TEXTFIELD_KEY,
                MetaDataLayerType.PROPERTY_FOOTER2_TEXTFIELD_DEFAULT);
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
                    currentLine = replaceStringVariables(currentLine, false, "<INFO=");
                    currentLine = replaceStringVariables(currentLine, false, "<META=");
                    currentLine = replaceStringVariables(currentLine, false, "<FILE_INFO=");
                    currentLine = replaceStringVariables(currentLine, false, "<BAND_INFO=");
                    currentLine = replaceStringVariables(currentLine, false, "<FILE_META=");
                    currentLine = replaceStringVariables(currentLine, false, "<BAND_META=");
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

    private String getLocationFooter2() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_FOOTER2_LOCATION_KEY,
                MetaDataLayerType.PROPERTY_FOOTER2_LOCATION_DEFAULT);
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


    private String getMyInfo1() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD1_KEY,
                MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD1_DEFAULT);
    }

    private String getMyInfo2() {
        return  getConfigurationProperty(MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD2_KEY,
                MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD2_DEFAULT);
    }

    private String getMyInfo3() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD3_KEY,
                MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD3_DEFAULT);
    }

    private String getMyInfo4() {
        return getConfigurationProperty(MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD4_KEY,
                MetaDataLayerType.PROPERTY_MY_INFO_TEXTFIELD4_DEFAULT);
    }

    private String getMyInfo() {
        StringBuilder sb = new StringBuilder();

        String myInfo1 = getMyInfo1();
        String myInfo2 = getMyInfo2();
        String myInfo3 = getMyInfo3();
        String myInfo4 = getMyInfo4();

        if (myInfo1 != null && myInfo1.length() > 1) {
            sb.append(myInfo1);
            sb.append(" ");
        }

        if (myInfo2 != null && myInfo2.length() > 1) {
            sb.append(myInfo2);
            sb.append(" ");
        }

        if (myInfo3 != null && myInfo3.length() > 1) {
            sb.append(myInfo3);
            sb.append(" ");
        }

        if (myInfo4 != null && myInfo4.length() > 1) {
            sb.append(myInfo4);
            sb.append(" ");
        }

        String myInfo = sb.toString();
        if (myInfo != null) {
            myInfo = myInfo.trim();
        }

        return myInfo;
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
