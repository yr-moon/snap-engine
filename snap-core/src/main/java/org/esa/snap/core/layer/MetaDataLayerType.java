
package org.esa.snap.core.layer;

import com.bc.ceres.binding.Property;
import com.bc.ceres.binding.PropertyContainer;
import com.bc.ceres.binding.PropertySet;
import com.bc.ceres.glayer.Layer;
import com.bc.ceres.glayer.LayerContext;
import com.bc.ceres.glayer.LayerType;
import com.bc.ceres.glayer.annotations.LayerTypeMetadata;
import org.esa.snap.core.datamodel.RasterDataNode;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Daniel Knowles
 */


@LayerTypeMetadata(name = "MetaDataLayerType", aliasNames = {"org.esa.snap.core.layer.MetaDataLayerType"})
public class MetaDataLayerType extends LayerType {


    // Parameters Section

    private static final String PROPERTY_ROOT_KEY = "header.footer.layer";
    private static final String PROPERTY_ROOT_ALIAS = "headerFooterLayer";

    private static final String PROPERTY_HEADER_ROOT_KEY = PROPERTY_ROOT_KEY + ".header.contents";
    private static final String PROPERTY_HEADER_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "headerContents";

    public static final String PROPERTY_HEADER_SECTION_KEY = PROPERTY_HEADER_ROOT_KEY + ".section";
    public static final String PROPERTY_HEADER_SECTION_LABEL = "Title Header";
    public static final String PROPERTY_HEADER_SECTION_TOOLTIP = "Contents of header";
    public static final String PROPERTY_HEADER_SECTION_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "Section";

    public static final String PROPERTY_HEADER_SHOW_KEY = PROPERTY_HEADER_ROOT_KEY + ".show";
    public static final String PROPERTY_HEADER_SHOW_LABEL = "Show Header";
    public static final String PROPERTY_HEADER_SHOW_TOOLTIP = "Show header";
    public static final String PROPERTY_HEADER_SHOW_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "Show";
    public static final boolean PROPERTY_HEADER_SHOW_DEFAULT = true;
    public static final Class PROPERTY_HEADER_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_HEADER_TEXTFIELD_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.textfield";
    public static final String PROPERTY_HEADER_TEXTFIELD_LABEL = "Header";
    public static final String PROPERTY_HEADER_TEXTFIELD_TOOLTIP = "Adds a title/header to the Header-Footer Layer";
    public static final String PROPERTY_HEADER_TEXTFIELD_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderTextfield";
    public static final String PROPERTY_HEADER_TEXTFIELD_DEFAULT = "Title: <META=title>";
    public static final Class PROPERTY_HEADER_TEXTFIELD_TYPE = String.class;

    public static final String PROPERTY_HEADER_TEXTFIELD2_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.textfield2";
    public static final String PROPERTY_HEADER_TEXTFIELD2_LABEL = "Header";
    public static final String PROPERTY_HEADER_TEXTFIELD2_TOOLTIP = "Adds a line to title/header to the Header-Footer Layer";
    public static final String PROPERTY_HEADER_TEXTFIELD2_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderTextfield2";
    public static final String PROPERTY_HEADER_TEXTFIELD2_DEFAULT = "Band: [BAND] ([BAND_DESCRIPTION])";
    public static final Class PROPERTY_HEADER_TEXTFIELD2_TYPE = String.class;

    public static final String PROPERTY_HEADER_TEXTFIELD3_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.textfield3";
    public static final String PROPERTY_HEADER_TEXTFIELD3_LABEL = "Header";
    public static final String PROPERTY_HEADER_TEXTFIELD3_TOOLTIP = "Adds a line to title/header to the Header-Footer Layer";
    public static final String PROPERTY_HEADER_TEXTFIELD3_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderTextfield3";
    public static final String PROPERTY_HEADER_TEXTFIELD3_DEFAULT = "ID: <META=id>";
    public static final Class PROPERTY_HEADER_TEXTFIELD3_TYPE = String.class;


    public static final String PROPERTY_HEADER_METADATA_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.metadata";
    public static final String PROPERTY_HEADER_METADATA_LABEL = "Header Metadata";
    public static final String PROPERTY_HEADER_METADATA_TOOLTIP = "Adds metadata to header";
    public static final String PROPERTY_HEADER_METADATA_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderMetadata";
    public static final String PROPERTY_HEADER_METADATA_DEFAULT = "";
    public static final Class PROPERTY_HEADER_METADATA_TYPE = String.class;

    public static final String PROPERTY_HEADER_METADATA_KEYS_SHOW_KEY = PROPERTY_HEADER_ROOT_KEY + ".metadata.keys.show";
    public static final String PROPERTY_HEADER_METADATA_KEYS_SHOW_LABEL = "Show Header Metadata Keys";
    public static final String PROPERTY_HEADER_METADATA_KEYS_SHOW_TOOLTIP = "Show header metadata keys";
    public static final String PROPERTY_HEADER_METADATA_KEYS_SHOW_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "MetadataKeysShow";
    public static final boolean PROPERTY_HEADER_METADATA_KEYS_SHOW_DEFAULT = false;
    public static final Class PROPERTY_HEADER_METADATA_KEYS_SHOW_TYPE = Boolean.class;




    private static final String PROPERTY_FOOTER_ROOT_KEY = PROPERTY_ROOT_KEY + ".footer.contents";
    private static final String PROPERTY_FOOTER_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "footerContents";

    public static final String PROPERTY_FOOTER_SECTION_KEY = PROPERTY_FOOTER_ROOT_KEY + ".section";
    public static final String PROPERTY_FOOTER_SECTION_LABEL = "Notes & Metadata";
    public static final String PROPERTY_FOOTER_SECTION_TOOLTIP = "Contents of footer";
    public static final String PROPERTY_FOOTER_SECTION_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "Section";

    public static final String PROPERTY_FOOTER_SHOW_KEY = PROPERTY_FOOTER_ROOT_KEY + ".show";
    public static final String PROPERTY_FOOTER_SHOW_LABEL = "Show Footer";
    public static final String PROPERTY_FOOTER_SHOW_TOOLTIP = "Show footer";
    public static final String PROPERTY_FOOTER_SHOW_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "Show";
    public static final boolean PROPERTY_FOOTER_SHOW_DEFAULT = true;
    public static final Class PROPERTY_FOOTER_SHOW_TYPE = Boolean.class;


    public static final String PROPERTY_FOOTER_TEXTFIELD_KEY = PROPERTY_FOOTER_ROOT_KEY + ".textfield";
    public static final String PROPERTY_FOOTER_TEXTFIELD_LABEL = "Footer";
    public static final String PROPERTY_FOOTER_TEXTFIELD_TOOLTIP = "Adds a footer to the Header-Footer Layer";
    public static final String PROPERTY_FOOTER_TEXTFIELD_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterTextfield";
    public static final String PROPERTY_FOOTER_TEXTFIELD_DEFAULT = "";
    public static final Class PROPERTY_FOOTER_TEXTFIELD_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata";
    public static final String PROPERTY_FOOTER_METADATA_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata";
    public static final String PROPERTY_FOOTER_METADATA_DEFAULT = "[FILE], [Title], [Sensor],[Platform], [File_Location]";
    public static final Class PROPERTY_FOOTER_METADATA_TYPE = String.class;


    public static final String PROPERTY_FOOTER_METADATA2_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata2";
    public static final String PROPERTY_FOOTER_METADATA2_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA2_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA2_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata2";
    public static final String PROPERTY_FOOTER_METADATA2_DEFAULT = "[SCENE_SIZE],[SCENE_START_TIME],[SCENE_END_TIME]";
    public static final Class PROPERTY_FOOTER_METADATA2_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA3_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata3";
    public static final String PROPERTY_FOOTER_METADATA3_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA3_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA3_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata3";
    public static final String PROPERTY_FOOTER_METADATA3_DEFAULT = "[BAND],[BAND_DESCRIPTION],[PRODUCT_TYPE]";
    public static final Class PROPERTY_FOOTER_METADATA3_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA4_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata4";
    public static final String PROPERTY_FOOTER_METADATA4_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA4_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA4_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata4";
    public static final String PROPERTY_FOOTER_METADATA4_DEFAULT = "[DAY_NIGHT],[ORBIT],[START_ORBIT],[END_ORBIT]";
    public static final Class PROPERTY_FOOTER_METADATA4_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA5_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata5";
    public static final String PROPERTY_FOOTER_METADATA5_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA5_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA5_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata5";
    public static final String PROPERTY_FOOTER_METADATA5_DEFAULT = "";
    public static final Class PROPERTY_FOOTER_METADATA5_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA_KEYS_SHOW_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata.keys.show";
    public static final String PROPERTY_FOOTER_METADATA_KEYS_SHOW_LABEL = "Show Footer Metadata Keys";
    public static final String PROPERTY_FOOTER_METADATA_KEYS_SHOW_TOOLTIP = "Show footer metadata keys";
    public static final String PROPERTY_FOOTER_METADATA_KEYS_SHOW_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "MetadataKeysShow";
    public static final boolean PROPERTY_FOOTER_METADATA_KEYS_SHOW_DEFAULT = true;
    public static final Class PROPERTY_FOOTER_METADATA_KEYS_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_FOOTER_METADATA_DELIMITER_KEY = PROPERTY_FOOTER_ROOT_KEY + ".metadata.delimiter";
    public static final String PROPERTY_FOOTER_METADATA_DELIMITER_LABEL = "Metadata Keys Delimiter";
    public static final String PROPERTY_FOOTER_METADATA_DELIMITER_TOOLTIP = "Delimiter to use when auto-displaying metadata key-value pairs";
    public static final String PROPERTY_FOOTER_METADATA_DELIMITER_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "MetadataDelimiter";
    public static final String PROPERTY_FOOTER_METADATA_DELIMITER_DEFAULT = ": ";
    public static final Class PROPERTY_FOOTER_METADATA_DELIMITER_TYPE = String.class;

    public static final String PROPERTY_FOOTER_ALL_METADATA_SHOW_KEY = PROPERTY_FOOTER_ROOT_KEY + ".show.all.metadata";
    public static final String PROPERTY_FOOTER_ALL_METADATA_SHOW_LABEL = "Show All Metadata";
    public static final String PROPERTY_FOOTER_ALL_METADATA_SHOW_TOOLTIP = "Display all metadata";
    public static final String PROPERTY_FOOTER_ALL_METADATA_SHOW_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "ShowAllMetadata";
    public static final boolean PROPERTY_FOOTER_ALL_METADATA_SHOW_DEFAULT = false;
    public static final Class PROPERTY_FOOTER_ALL_METADATA_SHOW_TYPE = Boolean.class;


    public static final String LOCATION_TOP_LEFT = "Top Left";
    public static final String LOCATION_TOP_CENTER = "Top Center";
    public static final String LOCATION_TOP_CENTER_JUSTIFY_LEFT = "Top Center Justify Left";
    public static final String LOCATION_TOP_RIGHT = "Top Right";
    public static final String LOCATION_BOTTOM_LEFT = "Bottom Left";
    public static final String LOCATION_BOTTOM_CENTER = "Bottom Center";
    public static final String LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT = "Bottom Center Justify Left";
    public static final String LOCATION_BOTTOM_RIGHT = "Bottom Right";
    public static final String LOCATION_RIGHT = "Right";
    public static final String LOCATION_LEFT = "Left";


    public static String[] getMetaDataLocationArray() {
        return  new String[]{
                LOCATION_TOP_LEFT,
                LOCATION_TOP_CENTER,
                LOCATION_TOP_CENTER_JUSTIFY_LEFT,
                LOCATION_TOP_RIGHT,
                LOCATION_BOTTOM_LEFT,
                LOCATION_BOTTOM_CENTER,
                LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT,
                LOCATION_BOTTOM_RIGHT,
                LOCATION_RIGHT,
                LOCATION_LEFT
        };
    }




    public static final String PROPERTY_FONT_STYLE_1 = "SanSerif";
    public static final String PROPERTY_FONT_STYLE_2 = "Serif";
    public static final String PROPERTY_FONT_STYLE_3 = "Courier";
    public static final String PROPERTY_FONT_STYLE_4 = "Monospaced";





    // Header Formatting Section

    private static final String PROPERTY_HEADER_FORMAT_ROOT_KEY = PROPERTY_ROOT_KEY + ".header.format";
    private static final String PROPERTY_HEADER_FORMAT_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "HeaderFormat";

    public static final String PROPERTY_HEADER_FORMAT_SECTION_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".section";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_LABEL = "Format: Title Header";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_TOOLTIP = "Formatting parameters for the header";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "Section";

    public static final String PROPERTY_HEADER_LOCATION_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".location";
    public static final String PROPERTY_HEADER_LOCATION_LABEL = "Header Location";
    public static final String PROPERTY_HEADER_LOCATION_TOOLTIP = "Where to place the header on the image";
    private static final String PROPERTY_HEADER_LOCATION_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "HeaderLocation";
    public static final String PROPERTY_HEADER_LOCATION_DEFAULT = LOCATION_TOP_CENTER;
    public static final Class PROPERTY_HEADER_LOCATION_TYPE = String.class;

    public static final String PROPERTY_HEADER_GAP_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".gap";
    public static final String PROPERTY_HEADER_GAP_LABEL = "Header Gap";
    public static final String PROPERTY_HEADER_GAP_TOOLTIP = "Percentage of scene size to place header away from the edge of the scene image";
    private static final String PROPERTY_HEADER_GAP_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "Offset";
    public static final Double PROPERTY_HEADER_GAP_DEFAULT = 6.0;
    public static final double PROPERTY_HEADER_GAP_MIN = -100;
    public static final double PROPERTY_HEADER_GAP_MAX = 100;
    public static final String PROPERTY_HEADER_GAP_INTERVAL = "[" + PROPERTY_HEADER_GAP_MIN + "," + PROPERTY_HEADER_GAP_MAX + "]";
    public static final Class PROPERTY_HEADER_GAP_TYPE = Double.class;

    public static final String PROPERTY_HEADER_FONT_SIZE_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.size";
    public static final String PROPERTY_HEADER_FONT_SIZE_LABEL = "Font Size";
    public static final String PROPERTY_HEADER_FONT_SIZE_TOOLTIP = "Set size of the header font";
    private static final String PROPERTY_HEADER_FONT_SIZE_ALIAS =  PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontSize";
    public static final int PROPERTY_HEADER_FONT_SIZE_DEFAULT = 30;
    public static final Class PROPERTY_HEADER_FONT_SIZE_TYPE = Integer.class;
    public static final int PROPERTY_HEADER_FONT_SIZE_VALUE_MIN = 6;
    public static final int PROPERTY_HEADER_FONT_SIZE_VALUE_MAX = 70;
    public static final String PROPERTY_HEADER_FONT_SIZE_INTERVAL = "[" + GraticuleLayerType.PROPERTY_LABELS_SIZE_VALUE_MIN + "," + GraticuleLayerType.PROPERTY_LABELS_SIZE_VALUE_MAX + "]";

    public static final String PROPERTY_HEADER_FONT_COLOR_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.color";
    public static final String PROPERTY_HEADER_FONT_COLOR_LABEL = "Font Color";
    public static final String PROPERTY_HEADER_FONT_COLOR_TOOLTIP = "Set color of the header text";
    private static final String PROPERTY_HEADER_FONT_COLOR_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontColor";
    public static final Color PROPERTY_HEADER_FONT_COLOR_DEFAULT = Color.BLACK;
    public static final Class PROPERTY_HEADER_FONT_COLOR_TYPE = Color.class;

    public static final String PROPERTY_HEADER_FONT_STYLE_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.name";
    public static final String PROPERTY_HEADER_FONT_STYLE_LABEL = "Font Name";
    public static final String PROPERTY_HEADER_FONT_STYLE_TOOLTIP = "Set the font name of the header";
    public static final String PROPERTY_HEADER_FONT_STYLE_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontName";
    public static final String PROPERTY_HEADER_FONT_STYLE_DEFAULT = "Serif";
    public static final Class PROPERTY_HEADER_FONT_STYLE_TYPE = String.class;
    public static final Object PROPERTY_HEADER_FONT_STYLE_VALUE_SET[] = {PROPERTY_FONT_STYLE_1, PROPERTY_FONT_STYLE_2, PROPERTY_FONT_STYLE_3, PROPERTY_FONT_STYLE_4};


    public static final String PROPERTY_HEADER_FONT_ITALIC_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.italic";
    public static final String PROPERTY_HEADER_FONT_ITALIC_LABEL = "Font Italic";
    public static final String PROPERTY_HEADER_FONT_ITALIC_TOOLTIP = "Format header text font in italic";
    public static final String PROPERTY_HEADER_FONT_ITALIC_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontItalic";
    public static final boolean PROPERTY_HEADER_FONT_ITALIC_DEFAULT = true;
    public static final Class PROPERTY_HEADER_FONT_ITALIC_TYPE = Boolean.class;

    public static final String PROPERTY_HEADER_FONT_BOLD_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.bold";
    public static final String PROPERTY_HEADER_FONT_BOLD_LABEL = "Font Bold";
    public static final String PROPERTY_HEADER_FONT_BOLD_TOOLTIP = "Format header text font in bold";
    public static final String PROPERTY_HEADER_FONT_BOLD_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontBold";
    public static final boolean PROPERTY_HEADER_FONT_BOLD_DEFAULT = false;
    public static final Class PROPERTY_HEADER_FONT_BOLD_TYPE = Boolean.class;






    // Footer Format Section


    private static final String PROPERTY_FOOTER_FORMAT_ROOT_KEY = PROPERTY_ROOT_KEY + ".footer.formatting";
    private static final String PROPERTY_FOOTER_FORMAT_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "FooterFormatting";

    public static final String PROPERTY_FOOTER_FORMATTING_SECTION_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".section";
    public static final String PROPERTY_FOOTER_FORMATTING_SECTION_LABEL = "Format: Notes & Metadata";
    public static final String PROPERTY_FOOTER_FORMATTING_SECTION_TOOLTIP = "Set  location of matadata on the scene image";
    public static final String PROPERTY_FOOTER_FORMATTING_SECTION_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "Section";

    public static final String PROPERTY_FOOTER_LOCATION_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".location";
    public static final String PROPERTY_FOOTER_LOCATION_LABEL = "Footer Location";
    public static final String PROPERTY_FOOTER_LOCATION_TOOLTIP = "Where to place the footer on the image";
    private static final String PROPERTY_FOOTER_LOCATION_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "FooterLocation";
    public static final String PROPERTY_FOOTER_LOCATION_DEFAULT = LOCATION_RIGHT;
    public static final Class PROPERTY_FOOTER_LOCATION_TYPE = String.class;

    public static final String PROPERTY_FOOTER_GAP_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".offset";
    public static final String PROPERTY_FOOTER_GAP_LABEL = "Footer Gap";
    public static final String PROPERTY_FOOTER_GAP_TOOLTIP = "Percentage of scene size to place metadata away from the edge of the scene image";
    private static final String PROPERTY_FOOTER_GAP_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "Offset";
    public static final Double PROPERTY_FOOTER_GAP_DEFAULT = 50.0;
    public static final double PROPERTY_FOOTER_GAP_MIN = -100;
    public static final double PROPERTY_FOOTER_GAP_MAX = 100;
    public static final String PROPERTY_FOOTER_GAP_INTERVAL = "[" + MetaDataLayerType.PROPERTY_FOOTER_GAP_MIN + "," + MetaDataLayerType.PROPERTY_FOOTER_GAP_MAX + "]";
    public static final Class PROPERTY_FOOTER_GAP_TYPE = Double.class;

    public static final String PROPERTY_FOOTER_FONT_SIZE_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".size";
    public static final String PROPERTY_FOOTER_FONT_SIZE_LABEL = "Font Size";
    public static final String PROPERTY_FOOTER_FONT_SIZE_TOOLTIP = "Set size of the footer text";
    private static final String PROPERTY_FOOTER_FONT_SIZE_ALIAS =  PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "Size";
    public static final int PROPERTY_FOOTER_FONT_SIZE_DEFAULT = 15;
    public static final Class PROPERTY_FOOTER_FONT_SIZE_TYPE = Integer.class;
    public static final int PROPERTY_FOOTER_FONT_SIZE_MIN = 6;
    public static final int PROPERTY_FOOTER_FONT_SIZE_MAX = 70;
    public static final String PROPERTY_FOOTER_FONT_SIZE_INTERVAL = "[" + PROPERTY_FOOTER_FONT_SIZE_MIN + "," + PROPERTY_FOOTER_FONT_SIZE_MAX + "]";

    public static final String PROPERTY_FOOTER_FONT_COLOR_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".font.color";
    public static final String PROPERTY_FOOTER_FONT_COLOR_LABEL = "Font Color";
    public static final String PROPERTY_FOOTER_FONT_COLOR_TOOLTIP = "Set color of the footer text";
    private static final String PROPERTY_FOOTER_FONT_COLOR_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "FontColor";
    public static final Color PROPERTY_FOOTER_FONT_COLOR_DEFAULT = Color.BLACK;
    public static final Class PROPERTY_FOOTER_FONT_COLOR_TYPE = Color.class;

    public static final String PROPERTY_FOOTER_FONT_STYLE_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".font.style";
    public static final String PROPERTY_FOOTER_FONT_STYLE_LABEL = "Font Style";
    public static final String PROPERTY_FOOTER_FONT_STYLE_TOOLTIP = "Set the font style of the footer";
    public static final String PROPERTY_FOOTER_FONT_STYLE_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "FontName";
    public static final String PROPERTY_FOOTER_FONT_STYLE_DEFAULT = "SanSerif";
    public static final Class PROPERTY_FOOTER_FONT_STYLE_TYPE = String.class;
    public static final Object PROPERTY_FOOTER_FONT_STYLE_VALUE_SET[] = {PROPERTY_FONT_STYLE_1, PROPERTY_FONT_STYLE_2, PROPERTY_FONT_STYLE_3, PROPERTY_FONT_STYLE_4};

    public static final String PROPERTY_FOOTER_FONT_ITALIC_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".font.italic";
    public static final String PROPERTY_FOOTER_FONT_ITALIC_LABEL = "Font Italic";
    public static final String PROPERTY_FOOTER_FONT_ITALIC_TOOLTIP = "Format footer text font in italic";
    public static final String PROPERTY_FOOTER_FONT_ITALIC_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "FontItalic";
    public static final boolean PROPERTY_FOOTER_FONT_ITALIC_DEFAULT = false;
    public static final Class PROPERTY_FOOTER_FONT_ITALIC_TYPE = Boolean.class;

    public static final String PROPERTY_FOOTER_FONT_BOLD_KEY = PROPERTY_FOOTER_FORMAT_ROOT_KEY + ".font.bold";
    public static final String PROPERTY_FOOTER_FONT_BOLD_LABEL = "Font Bold";
    public static final String PROPERTY_FOOTER_FONT_BOLD_TOOLTIP = "Format footer text font in bold";
    public static final String PROPERTY_FOOTER_FONT_BOLD_ALIAS = PROPERTY_FOOTER_FORMAT_ROOT_ALIAS + "FontBold";
    public static final boolean PROPERTY_FOOTER_FONT_BOLD_DEFAULT = false;
    public static final Class PROPERTY_FOOTER_FONT_BOLD_TYPE = Boolean.class;



    // ---------------------------------------------------------

    public static final String PROPERTY_NAME_RASTER = "raster";



    // Property Setting: Restore Defaults
    public static final String PROPERTY_RESTORE_DEFAULTS_NAME = "metadata.layer.restoreDefaults";
    public static final String PROPERTY_RESTORE_TO_DEFAULTS_LABEL = "RESTORE DEFAULTS (Metadata Layer Preferences)";
    public static final String PROPERTY_RESTORE_TO_DEFAULTS_TOOLTIP = "Restore all metadata layer preferences to the default";
    public static final boolean PROPERTY_RESTORE_TO_DEFAULTS_DEFAULT = false;


    /**
     * @deprecated since BEAM 4.7, no replacement; kept for compatibility of sessions
     */
    @Deprecated
    private static final String PROPERTY_NAME_TRANSFORM = "imageToModelTransform";


    @Override
    public boolean isValidFor(LayerContext ctx) {
        return true;
    }

    @Override
    public Layer createLayer(LayerContext ctx, PropertySet configuration) {
//        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        return new MetaDataLayer(this, (RasterDataNode) configuration.getValue(PROPERTY_NAME_RASTER),
                configuration);
    }

    @Override
    public PropertySet createLayerConfig(LayerContext ctx) {
        final PropertyContainer vc = new PropertyContainer();

        // Parameters Section

        final Property parametersSectionModel = Property.create(PROPERTY_HEADER_SECTION_KEY, Boolean.class, true, true);
        parametersSectionModel.getDescriptor().setAlias(PROPERTY_HEADER_SECTION_ALIAS);
        vc.addProperty(parametersSectionModel);

        final Property headerShowModel = Property.create(PROPERTY_HEADER_SHOW_KEY, PROPERTY_HEADER_SHOW_TYPE, true, true);
        headerShowModel.getDescriptor().setAlias(PROPERTY_HEADER_SHOW_ALIAS);
        vc.addProperty(headerShowModel);

        final Property headerModel = Property.create(PROPERTY_HEADER_TEXTFIELD_KEY, PROPERTY_HEADER_TEXTFIELD_TYPE, true, true);
        headerModel.getDescriptor().setAlias(PROPERTY_HEADER_TEXTFIELD_ALIAS);
        vc.addProperty(headerModel);

        final Property header2Model = Property.create(PROPERTY_HEADER_TEXTFIELD2_KEY, PROPERTY_HEADER_TEXTFIELD2_TYPE, true, true);
        header2Model.getDescriptor().setAlias(PROPERTY_HEADER_TEXTFIELD2_ALIAS);
        vc.addProperty(header2Model);

        final Property header3Model = Property.create(PROPERTY_HEADER_TEXTFIELD3_KEY, PROPERTY_HEADER_TEXTFIELD3_TYPE, true, true);
        header3Model.getDescriptor().setAlias(PROPERTY_HEADER_TEXTFIELD3_ALIAS);
        vc.addProperty(header3Model);

        final Property headerMetadataModel = Property.create(PROPERTY_HEADER_METADATA_KEY, PROPERTY_HEADER_METADATA_TYPE, true, true);
        headerMetadataModel.getDescriptor().setAlias(PROPERTY_HEADER_METADATA_ALIAS);
        vc.addProperty(headerMetadataModel);

        final Property headerMetadataKeysShowModel = Property.create(PROPERTY_HEADER_METADATA_KEYS_SHOW_KEY, PROPERTY_HEADER_METADATA_KEYS_SHOW_TYPE, true, true);
        headerMetadataKeysShowModel.getDescriptor().setAlias(PROPERTY_HEADER_METADATA_KEYS_SHOW_ALIAS);
        vc.addProperty(headerMetadataKeysShowModel);


        final Property footerParametersSectionModel = Property.create(PROPERTY_FOOTER_SECTION_KEY, Boolean.class, true, true);
        footerParametersSectionModel.getDescriptor().setAlias(PROPERTY_FOOTER_SECTION_ALIAS);
        vc.addProperty(footerParametersSectionModel);

        final Property footerShowModel = Property.create(PROPERTY_FOOTER_SHOW_KEY, PROPERTY_FOOTER_SHOW_TYPE, true, true);
        footerShowModel.getDescriptor().setAlias(PROPERTY_FOOTER_SHOW_ALIAS);
        vc.addProperty(footerShowModel);

        final Property displayAllMetadataModel = Property.create(PROPERTY_FOOTER_ALL_METADATA_SHOW_KEY, PROPERTY_FOOTER_ALL_METADATA_SHOW_TYPE, true, true);
        displayAllMetadataModel.getDescriptor().setAlias(PROPERTY_FOOTER_ALL_METADATA_SHOW_ALIAS);
        vc.addProperty(displayAllMetadataModel);



        final Property footerModel = Property.create(PROPERTY_FOOTER_TEXTFIELD_KEY, PROPERTY_FOOTER_TEXTFIELD_TYPE, true, true);
        footerModel.getDescriptor().setAlias(PROPERTY_FOOTER_TEXTFIELD_ALIAS);
        vc.addProperty(footerModel);

        final Property footerMetadataModel = Property.create(PROPERTY_FOOTER_METADATA_KEY, PROPERTY_FOOTER_METADATA_TYPE, true, true);
        footerMetadataModel.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA_ALIAS);
        vc.addProperty(footerMetadataModel);

        final Property footerMetadata2Model = Property.create(PROPERTY_FOOTER_METADATA2_KEY, PROPERTY_FOOTER_METADATA2_TYPE, true, true);
        footerMetadata2Model.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA2_ALIAS);
        vc.addProperty(footerMetadata2Model);

        final Property footerMetadata3Model = Property.create(PROPERTY_FOOTER_METADATA3_KEY, PROPERTY_FOOTER_METADATA3_TYPE, true, true);
        footerMetadata3Model.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA3_ALIAS);
        vc.addProperty(footerMetadata3Model);

        final Property footerMetadata4Model = Property.create(PROPERTY_FOOTER_METADATA4_KEY, PROPERTY_FOOTER_METADATA4_TYPE, true, true);
        footerMetadata4Model.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA4_ALIAS);
        vc.addProperty(footerMetadata4Model);

        final Property footerMetadata5Model = Property.create(PROPERTY_FOOTER_METADATA5_KEY, PROPERTY_FOOTER_METADATA5_TYPE, true, true);
        footerMetadata5Model.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA5_ALIAS);
        vc.addProperty(footerMetadata5Model);

        final Property footerMetadataKeysShowModel = Property.create(PROPERTY_FOOTER_METADATA_KEYS_SHOW_KEY, PROPERTY_FOOTER_METADATA_KEYS_SHOW_TYPE, true, true);
        footerMetadataKeysShowModel.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA_KEYS_SHOW_ALIAS);
        vc.addProperty(footerMetadataKeysShowModel);

        final Property footerMetadataDelimiterModel = Property.create(PROPERTY_FOOTER_METADATA_DELIMITER_KEY, PROPERTY_FOOTER_METADATA_DELIMITER_TYPE, true, true);
        footerMetadataDelimiterModel.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA_DELIMITER_ALIAS);
        vc.addProperty(footerMetadataDelimiterModel);




        // Header Formatting Section

        final Property headerFormatSectionModel = Property.create(PROPERTY_HEADER_FORMAT_SECTION_KEY, Boolean.class, true, true);
        headerFormatSectionModel.getDescriptor().setAlias(PROPERTY_HEADER_FORMAT_SECTION_ALIAS);
        vc.addProperty(headerFormatSectionModel);

        final Property locationModel = Property.create(PROPERTY_HEADER_LOCATION_KEY, PROPERTY_HEADER_LOCATION_TYPE, true, true);
        locationModel.getDescriptor().setAlias(PROPERTY_HEADER_LOCATION_ALIAS);
        vc.addProperty(locationModel);

        final Property headerGapFactorModel = Property.create(PROPERTY_HEADER_GAP_KEY, PROPERTY_HEADER_GAP_TYPE, true, true);
        headerGapFactorModel.getDescriptor().setAlias(PROPERTY_HEADER_GAP_ALIAS);
        vc.addProperty(headerGapFactorModel);

        final Property textFontSizeModel = Property.create(PROPERTY_HEADER_FONT_SIZE_KEY, Integer.class, PROPERTY_HEADER_FONT_SIZE_DEFAULT, true);
        textFontSizeModel.getDescriptor().setAlias(PROPERTY_HEADER_FONT_SIZE_ALIAS);
        vc.addProperty(textFontSizeModel);

        final Property textFgColorModel = Property.create(PROPERTY_HEADER_FONT_COLOR_KEY, Color.class, PROPERTY_HEADER_FONT_COLOR_DEFAULT, true);
        textFgColorModel.getDescriptor().setAlias(PROPERTY_HEADER_FONT_COLOR_ALIAS);
        vc.addProperty(textFgColorModel);

        final Property textFontModel = Property.create(PROPERTY_HEADER_FONT_STYLE_KEY, String.class, PROPERTY_HEADER_FONT_STYLE_DEFAULT, true);
        textFontModel.getDescriptor().setAlias(PROPERTY_HEADER_FONT_STYLE_ALIAS);
        vc.addProperty(textFontModel);

        final Property textFontItalicModel = Property.create(PROPERTY_HEADER_FONT_ITALIC_KEY, Boolean.class, PROPERTY_HEADER_FONT_ITALIC_DEFAULT, true);
        textFontItalicModel.getDescriptor().setAlias(PROPERTY_HEADER_FONT_ITALIC_ALIAS);
        vc.addProperty(textFontItalicModel);

        final Property textFontBoldModel = Property.create(PROPERTY_HEADER_FONT_BOLD_KEY, Boolean.class, PROPERTY_HEADER_FONT_BOLD_DEFAULT, true);
        textFontBoldModel.getDescriptor().setAlias(PROPERTY_HEADER_FONT_BOLD_ALIAS);
        vc.addProperty(textFontBoldModel);




        // Footer Formatting Section

        final Property locationSectionModel = Property.create(PROPERTY_FOOTER_FORMATTING_SECTION_KEY, Boolean.class, true, true);
        locationSectionModel.getDescriptor().setAlias(PROPERTY_FOOTER_FORMATTING_SECTION_ALIAS);
        vc.addProperty(locationSectionModel);

        final Property footerLocationModel = Property.create(PROPERTY_FOOTER_LOCATION_KEY, PROPERTY_FOOTER_LOCATION_TYPE, true, true);
        footerLocationModel.getDescriptor().setAlias(PROPERTY_FOOTER_LOCATION_ALIAS);
        vc.addProperty(footerLocationModel);

        final Property footerGapFactorModel = Property.create(PROPERTY_FOOTER_GAP_KEY, PROPERTY_FOOTER_GAP_TYPE, true, true);
        footerGapFactorModel.getDescriptor().setAlias(PROPERTY_FOOTER_GAP_ALIAS);
        vc.addProperty(footerGapFactorModel);

        final Property footerFontSizeModel = Property.create(PROPERTY_FOOTER_FONT_SIZE_KEY, Integer.class, PROPERTY_FOOTER_FONT_SIZE_DEFAULT, true);
        footerFontSizeModel.getDescriptor().setAlias(PROPERTY_FOOTER_FONT_SIZE_ALIAS);
        vc.addProperty(footerFontSizeModel);

        final Property footerFontColorModel = Property.create(PROPERTY_FOOTER_FONT_COLOR_KEY, Color.class, PROPERTY_FOOTER_FONT_COLOR_DEFAULT, true);
        footerFontColorModel.getDescriptor().setAlias(PROPERTY_FOOTER_FONT_COLOR_ALIAS);
        vc.addProperty(footerFontColorModel);

        final Property footerFontStyleModel = Property.create(PROPERTY_FOOTER_FONT_STYLE_KEY, String.class, PROPERTY_FOOTER_FONT_STYLE_DEFAULT, true);
        footerFontStyleModel.getDescriptor().setAlias(PROPERTY_FOOTER_FONT_STYLE_ALIAS);
        vc.addProperty(footerFontStyleModel);

        final Property footerFontItalicModel = Property.create(PROPERTY_FOOTER_FONT_ITALIC_KEY, Boolean.class, PROPERTY_FOOTER_FONT_ITALIC_DEFAULT, true);
        footerFontItalicModel.getDescriptor().setAlias(PROPERTY_FOOTER_FONT_ITALIC_ALIAS);
        vc.addProperty(footerFontItalicModel);

        final Property footerFontBoldModel = Property.create(PROPERTY_FOOTER_FONT_BOLD_KEY, Boolean.class, PROPERTY_FOOTER_FONT_BOLD_DEFAULT, true);
        footerFontBoldModel.getDescriptor().setAlias(PROPERTY_FOOTER_FONT_BOLD_ALIAS);
        vc.addProperty(footerFontBoldModel);


        final Property rasterModel = Property.create(PROPERTY_NAME_RASTER, RasterDataNode.class);
        rasterModel.getDescriptor().setNotNull(true);
        vc.addProperty(rasterModel);

        final Property transformModel = Property.create(PROPERTY_NAME_TRANSFORM, new AffineTransform());
        transformModel.getDescriptor().setTransient(true);
        vc.addProperty(transformModel);





        return vc;
    }
}
