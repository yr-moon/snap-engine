
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
    public static final String PROPERTY_HEADER_SECTION_LABEL = "Header Contents";
    public static final String PROPERTY_HEADER_SECTION_TOOLTIP = "Contents of header";
    public static final String PROPERTY_HEADER_SECTION_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "Section";

    public static final String PROPERTY_HEADER_SHOW_KEY = PROPERTY_HEADER_ROOT_KEY + ".show";
    public static final String PROPERTY_HEADER_SHOW_LABEL = "Show Footer";
    public static final String PROPERTY_HEADER_SHOW_TOOLTIP = "Show footer";
    public static final String PROPERTY_HEADER_SHOW_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "Show";
    public static final boolean PROPERTY_HEADER_SHOW_DEFAULT = true;
    public static final Class PROPERTY_HEADER_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_HEADER_TEXTFIELD_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.textfield";
    public static final String PROPERTY_HEADER_TEXTFIELD_LABEL = "Header";
    public static final String PROPERTY_HEADER_TEXTFIELD_TOOLTIP = "Adds a title/header to the Header-Footer Layer";
    public static final String PROPERTY_HEADER_TEXTFIELD_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderTextfield";
    public static final String PROPERTY_HEADER_TEXTFIELD_DEFAULT = "File: [FILE] - Band: [BAND]";
    public static final Class PROPERTY_HEADER_TEXTFIELD_TYPE = String.class;

    public static final String PROPERTY_HEADER2_TEXTFIELD_KEY = PROPERTY_HEADER_ROOT_KEY + ".header.textfield2";
    public static final String PROPERTY_HEADER2_TEXTFIELD_LABEL = "Header 2";
    public static final String PROPERTY_HEADER2_TEXTFIELD_TOOLTIP = "Adds a second line to title/header to the Header-Footer Layer";
    public static final String PROPERTY_HEADER2_TEXTFIELD_ALIAS = PROPERTY_HEADER_ROOT_ALIAS + "HeaderTextfield2";
    public static final String PROPERTY_HEADER2_TEXTFIELD_DEFAULT = "File: [FILE] \n Band: [BAND]";
    public static final Class PROPERTY_HEADER2_TEXTFIELD_TYPE = String.class;


    private static final String PROPERTY_FOOTER_ROOT_KEY = PROPERTY_ROOT_KEY + ".footer.contents";
    private static final String PROPERTY_FOOTER_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "footerContents";

    public static final String PROPERTY_FOOTER_SECTION_KEY = PROPERTY_FOOTER_ROOT_KEY + ".section";
    public static final String PROPERTY_FOOTER_LABEL = "Footer Contents";
    public static final String PROPERTY_FOOTER_TOOLTIP = "Contents of footer";
    public static final String PROPERTY_FOOTER_SECTION_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "Section";

    public static final String PROPERTY_FOOTER_SHOW_KEY = PROPERTY_FOOTER_ROOT_KEY + ".show";
    public static final String PROPERTY_FOOTER_SHOW_LABEL = "Show Footer";
    public static final String PROPERTY_FOOTER_SHOW_TOOLTIP = "Show footer";
    public static final String PROPERTY_FOOTER_SHOW_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "Show";
    public static final boolean PROPERTY_FOOTER_SHOW_DEFAULT = true;
    public static final Class PROPERTY_FOOTER_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_FOOTER_TEXTFIELD_KEY = PROPERTY_FOOTER_ROOT_KEY + ".footer.textfield";
    public static final String PROPERTY_FOOTER_TEXTFIELD_LABEL = "Footer";
    public static final String PROPERTY_FOOTER_TEXTFIELD_TOOLTIP = "Adds a footer to the Header-Footer Layer";
    public static final String PROPERTY_FOOTER_TEXTFIELD_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterTextfield";
    public static final String PROPERTY_FOOTER_TEXTFIELD_DEFAULT = "File: [FILE] <br>Band: [BAND]";
    public static final Class PROPERTY_FOOTER_TEXTFIELD_TYPE = String.class;

    public static final String PROPERTY_FOOTER_METADATA_KEY = PROPERTY_FOOTER_ROOT_KEY + ".footer.metadata";
    public static final String PROPERTY_FOOTER_METADATA_LABEL = "Footer Metadata";
    public static final String PROPERTY_FOOTER_METADATA_TOOLTIP = "Adds metadata to footer";
    public static final String PROPERTY_FOOTER_METADATA_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FooterMetadata";
    public static final String PROPERTY_FOOTER_METADATA_DEFAULT = "title, id";
    public static final Class PROPERTY_FOOTER_METADATA_TYPE = String.class;

    public static final String PROPERTY_FILE_NAME_KEY = PROPERTY_FOOTER_ROOT_KEY + ".filename";
    public static final String PROPERTY_FILE_NAME_LABEL = "Filename";
    public static final String PROPERTY_FILE_NAME_TOOLTIP = "Display filename";
    public static final String PROPERTY_FILE_NAME_ALIAS = PROPERTY_FOOTER_ROOT_ALIAS + "FileName";
    public static final boolean PROPERTY_FILE_NAME_DEFAULT = true;
    public static final Class PROPERTY_FILE_NAME_TYPE = Boolean.class;


    // MetaData Location Section

    private static final String PROPERTY_LOCATION_ROOT_KEY = PROPERTY_ROOT_KEY + ".location";
    private static final String PROPERTY_LOCATION_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "Location";

    public static final String PROPERTY_LOCATION_SECTION_KEY = PROPERTY_LOCATION_ROOT_KEY + ".section";
    public static final String PROPERTY_LOCATION_SECTION_LABEL = "Location";
    public static final String PROPERTY_LOCATION_SECTION_TOOLTIP = "Set  location of matadata on the scene image";
    public static final String PROPERTY_LOCATION_SECTION_ALIAS = PROPERTY_LOCATION_ROOT_ALIAS + "Section";

    public static final String LOCATION_TOP_LEFT = "Top Left";
    public static final String LOCATION_TOP_CENTER = "Top Center";
    public static final String LOCATION_TOP_CENTER_JUSTIFY_LEFT = "Top Center Justify Left";
    public static final String LOCATION_TOP_RIGHT = "Top Right";
    public static final String LOCATION_BOTTOM_LEFT = "Bottom Left";
    public static final String LOCATION_BOTTOM_CENTER = "Bottom Center";
    public static final String LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT = "Bottom Center Justify Left";
    public static final String LOCATION_BOTTOM_RIGHT = "Bottom Right";


    public static String[] getMetaDataLocationArray() {
        return  new String[]{
                LOCATION_TOP_LEFT,
                LOCATION_TOP_CENTER,
                LOCATION_TOP_CENTER_JUSTIFY_LEFT,
                LOCATION_TOP_RIGHT,
                LOCATION_BOTTOM_LEFT,
                LOCATION_BOTTOM_CENTER,
                LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT,
                LOCATION_BOTTOM_RIGHT
        };
    }


    public static final String PROPERTY_LOCATION_KEY = PROPERTY_LOCATION_ROOT_KEY + ".location";
    public static final String PROPERTY_LOCATION_LABEL = "Header Location";
    public static final String PROPERTY_LOCATION_TOOLTIP = "Where to place the header on the image";
    private static final String PROPERTY_LOCATION_ALIAS = PROPERTY_LOCATION_ROOT_ALIAS + "HeaderLocation";
    public static final String PROPERTY_LOCATION_DEFAULT = LOCATION_TOP_CENTER_JUSTIFY_LEFT;
    public static final Class PROPERTY_LOCATION_TYPE = String.class;

    public static final String PROPERTY_FOOTER_LOCATION_KEY = PROPERTY_LOCATION_ROOT_KEY + ".footer.location";
    public static final String PROPERTY_FOOTER_LOCATION_LABEL = "Footer Location";
    public static final String PROPERTY_FOOTER_LOCATION_TOOLTIP = "Where to place the footer on the image";
    private static final String PROPERTY_FOOTER_LOCATION_ALIAS = PROPERTY_LOCATION_ROOT_ALIAS + "FooterLocation";
    public static final String PROPERTY_FOOTER_LOCATION_DEFAULT = LOCATION_BOTTOM_CENTER_JUSTIFY_LEFT;
    public static final Class PROPERTY_FOOTER_LOCATION_TYPE = String.class;


    public static final String PROPERTY_LOCATION_GAP_FACTOR_KEY = PROPERTY_LOCATION_ROOT_KEY + ".offset";
    public static final String PROPERTY_LOCATION_GAP_FACTOR_LABEL = "Location Offset";
    public static final String PROPERTY_LOCATION_GAP_FACTOR_TOOLTIP = "Percentage of scene size to place metadata away from the edge of the scene image";
    private static final String PROPERTY_LOCATION_GAP_FACTOR_ALIAS = PROPERTY_LOCATION_ROOT_ALIAS + "Offset";
    public static final Double PROPERTY_LOCATION_GAP_FACTOR_DEFAULT = 15.0;
    public static final double PROPERTY_LOCATION_GAP_FACTOR_MIN = -100;
    public static final double PROPERTY_LOCATION_GAP_FACTOR_MAX = 100;
    public static final String PROPERTY_LOCATION_GAP_FACTOR_INTERVAL = "[" + MetaDataLayerType.PROPERTY_LOCATION_GAP_FACTOR_MIN + "," + MetaDataLayerType.PROPERTY_LOCATION_GAP_FACTOR_MAX + "]";
    public static final Class PROPERTY_LOCATION_GAP_FACTOR_TYPE = Double.class;




    // Header Formatting Section

    private static final String PROPERTY_HEADER_FORMAT_ROOT_KEY = PROPERTY_ROOT_KEY + ".header.format";
    private static final String PROPERTY_HEADER_FORMAT_ROOT_ALIAS = PROPERTY_ROOT_ALIAS + "HeaderFormat";

    public static final String PROPERTY_HEADER_FORMAT_SECTION_KEY = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".section";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_LABEL = "Header Formatting";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_TOOLTIP = "Formatting parameters for the header";
    public static final String PROPERTY_HEADER_FORMAT_SECTION_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "Section";

    public static final String PROPERTY_LABELS_ITALIC_NAME = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.italic";
    public static final String PROPERTY_LABELS_ITALIC_LABEL = "Italic";
    public static final String PROPERTY_LABELS_ITALIC_TOOLTIP = "Format header text font in italic";
    public static final String PROPERTY_LABELS_ITALIC_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontItalic";
    public static final boolean PROPERTY_LABELS_ITALIC_DEFAULT = false;
    public static final Class PROPERTY_LABELS_ITALIC_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_BOLD_NAME = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.bold";
    public static final String PROPERTY_LABELS_BOLD_LABEL = "Bold";
    public static final String PROPERTY_LABELS_BOLD_TOOLTIP = "Format header text font in bold";
    public static final String PROPERTY_LABELS_BOLD_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontBold";
    public static final boolean PROPERTY_LABELS_BOLD_DEFAULT = false;
    public static final Class PROPERTY_LABELS_BOLD_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_FONT_NAME = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".font.name";
    public static final String PROPERTY_LABELS_FONT_LABEL = "Font";
    public static final String PROPERTY_LABELS_FONT_TOOLTIP = "Set the text font of the labels";
    public static final String PROPERTY_LABELS_FONT_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "FontName";
    public static final String PROPERTY_LABELS_FONT_DEFAULT = "SanSerif";
    public static final Class PROPERTY_LABELS_FONT_TYPE = String.class;
    public static final String PROPERTY_LABELS_FONT_VALUE_1 = "SanSerif";
    public static final String PROPERTY_LABELS_FONT_VALUE_2 = "Serif";
    public static final String PROPERTY_LABELS_FONT_VALUE_3 = "Courier";
    public static final String PROPERTY_LABELS_FONT_VALUE_4 = "Monospaced";
    public static final Object PROPERTY_LABELS_FONT_VALUE_SET[] = {PROPERTY_LABELS_FONT_VALUE_1, PROPERTY_LABELS_FONT_VALUE_2, PROPERTY_LABELS_FONT_VALUE_3, PROPERTY_LABELS_FONT_VALUE_4};


    public static final String PROPERTY_LABELS_SIZE_NAME = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".labels.size";
    public static final String PROPERTY_LABELS_SIZE_LABEL = "Size";
    public static final String PROPERTY_LABELS_SIZE_TOOLTIP = "Set size of the header text";
    private static final String PROPERTY_LABELS_SIZE_ALIAS =  PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "LabelsSize";
    public static final int PROPERTY_LABELS_SIZE_DEFAULT = 18;
    public static final Class PROPERTY_LABELS_SIZE_TYPE = Integer.class;
    public static final int PROPERTY_LABELS_SIZE_VALUE_MIN = 6;
    public static final int PROPERTY_LABELS_SIZE_VALUE_MAX = 70;
    public static final String PROPERTY_LABELS_SIZE_INTERVAL = "[" + GraticuleLayerType.PROPERTY_LABELS_SIZE_VALUE_MIN + "," + GraticuleLayerType.PROPERTY_LABELS_SIZE_VALUE_MAX + "]";


    public static final String PROPERTY_LABELS_COLOR_NAME = PROPERTY_HEADER_FORMAT_ROOT_KEY + ".labels.color";
    public static final String PROPERTY_LABELS_COLOR_LABEL = "Color";
    public static final String PROPERTY_LABELS_COLOR_TOOLTIP = "Set color of the header text";
    private static final String PROPERTY_LABELS_COLOR_ALIAS = PROPERTY_HEADER_FORMAT_ROOT_ALIAS + "Color";
    public static final Color PROPERTY_LABELS_COLOR_DEFAULT = Color.BLACK;
    public static final Class PROPERTY_LABELS_COLOR_TYPE = Color.class;




    // Property Settings: Grid Spacing Section

    public static final String PROPERTY_GRID_SPACING_SECTION_NAME = "graticule.grid.spacing.section";
    public static final String PROPERTY_GRID_SPACING_SECTION_LABEL = "Grid Spacing";
    public static final String PROPERTY_GRID_SPACING_SECTION_TOOLTIP = "Set grid spacing in degrees (0=AUTOSPACING)";
    public static final String PROPERTY_GRID_SPACING_SECTION_ALIAS = "graticuleGridSpacingSection";

    public static final String PROPERTY_GRID_SPACING_LAT_NAME = "graticule.spacing.lat";
    public static final String PROPERTY_GRID_SPACING_LAT_LABEL = "Latitude";
    public static final String PROPERTY_GRID_SPACING_LAT_TOOLTIP = "Set latitude grid spacing in degrees (0=AUTOSPACING)";
    private static final String PROPERTY_GRID_SPACING_LAT_ALIAS = "graticuleSpacingLat";
    public static final double PROPERTY_GRID_SPACING_LAT_DEFAULT = 0.0;
    public static final Class PROPERTY_GRID_SPACING_LAT_TYPE = Double.class;

    public static final String PROPERTY_GRID_SPACING_LON_NAME = "graticule.spacing.lon";
    public static final String PROPERTY_GRID_SPACING_LON_LABEL = "Longitude";
    public static final String PROPERTY_GRID_SPACING_LON_TOOLTIP = "Set longitude grid spacing in degrees (0=AUTOSPACING)";
    private static final String PROPERTY_GRID_SPACING_LON_ALIAS = "graticuleSpacingLon";
    public static final double PROPERTY_GRID_SPACING_LON_DEFAULT = 0.0;
    public static final Class PROPERTY_GRID_SPACING_LON_TYPE = Double.class;


    // Property Settings: Labels Section

    public static final String PROPERTY_LABELS_SECTION_NAME = "graticule.labels.section";
    public static final String PROPERTY_LABELS_SECTION_LABEL = "Labels";
    public static final String PROPERTY_LABELS_SECTION_TOOLTIP = "Configuration options for the labels";
    public static final String PROPERTY_LABELS_SECTION_ALIAS = "graticuleLabelsSection";


    public static final String PROPERTY_LABELS_NORTH_NAME = "graticule.labels.north";
    public static final String PROPERTY_LABELS_NORTH_LABEL = "North";
    public static final String PROPERTY_LABELS_NORTH_TOOLTIP = "Display north labels";
    public static final String PROPERTY_LABELS_NORTH_ALIAS = "labelsNorth";
    public static final boolean PROPERTY_LABELS_NORTH_DEFAULT = true;
    public static final Class PROPERTY_LABELS_NORTH_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_SOUTH_NAME = "graticule.labels.south";
    public static final String PROPERTY_LABELS_SOUTH_LABEL = "South";
    public static final String PROPERTY_LABELS_SOUTH_TOOLTIP = "Display south labels";
    public static final String PROPERTY_LABELS_SOUTH_ALIAS = "labelsSouth";
    public static final boolean PROPERTY_LABELS_SOUTH_DEFAULT = true;
    public static final Class PROPERTY_LABELS_SOUTH_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_WEST_NAME = "graticule.labels.west";
    public static final String PROPERTY_LABELS_WEST_LABEL = "West";
    public static final String PROPERTY_LABELS_WEST_TOOLTIP = "Display west labels";
    public static final String PROPERTY_LABELS_WEST_ALIAS = "labelsWest";
    public static final boolean PROPERTY_LABELS_WEST_DEFAULT = true;
    public static final Class PROPERTY_LABELS_WEST_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_EAST_NAME = "graticule.labels.east";
    public static final String PROPERTY_LABELS_EAST_LABEL = "East";
    public static final String PROPERTY_LABELS_EAST_TOOLTIP = "Display east labels";
    public static final String PROPERTY_LABELS_EAST_ALIAS = "labelsEast";
    public static final boolean PROPERTY_LABELS_EAST_DEFAULT = true;
    public static final Class PROPERTY_LABELS_EAST_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_INSIDE_NAME = "graticule.labels.inside";
    public static final String PROPERTY_LABELS_INSIDE_LABEL = "Put on Inside";
    public static final String PROPERTY_LABELS_INSIDE_TOOLTIP = "Put on labels inside of the data image (also see backdrop options below)";
    private static final String PROPERTY_LABELS_INSIDE_ALIAS = "labelsInside";
    public static final boolean PROPERTY_LABELS_INSIDE_DEFAULT = false;
    public static final Class PROPERTY_LABELS_INSIDE_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_SUFFIX_NSWE_NAME = "graticule.labels.suffix.nswe";
    public static final String PROPERTY_LABELS_SUFFIX_NSWE_LABEL = "Suffix (N,S,W,E)";
    public static final String PROPERTY_LABELS_SUFFIX_NSWE_TOOLTIP = "Format label text with suffix (N,S,W,E) instead of (+/-)";
    private static final String PROPERTY_LABELS_SUFFIX_NSWE_ALIAS = "graticuleLabelsSuffixNswe";
    public static final boolean PROPERTY_LABELS_SUFFIX_NSWE_DEFAULT = false;
    public static final Class PROPERTY_LABELS_SUFFIX_NSWE_TYPE = Boolean.class;

    public static final String PROPERTY_LABELS_DECIMAL_VALUE_NAME = "graticule.labels.decimal.value";
    public static final String PROPERTY_LABELS_DECIMAL_VALUE_LABEL = "Decimal Value";
    public static final String PROPERTY_LABELS_DECIMAL_VALUE_TOOLTIP = "Format label text with decimal value instead of degrees/minutes/seconds";
    private static final String PROPERTY_LABELS_DECIMAL_VALUE_ALIAS = "graticuleLabelsDecimalValue";
    public static final boolean PROPERTY_LABELS_DECIMAL_VALUE_DEFAULT = false;
    public static final Class PROPERTY_LABELS_DECIMAL_VALUE_TYPE = Boolean.class;



    public static final String PROPERTY_LABELS_ROTATION_LON_NAME = "graticule.labels.rotation.lon";
    public static final String PROPERTY_LABELS_ROTATION_LON_LABEL = "Rotation (Longitude)";
    public static final String PROPERTY_LABELS_ROTATION_LON_TOOLTIP = "Rotate longitude labels (0 degrees = perpendicular)";
    private static final String PROPERTY_LABELS_ROTATION_LON_ALIAS = "labelsRotationLon";
    public static final double PROPERTY_LABELS_ROTATION_LON_DEFAULT = 90;
    public static final Class PROPERTY_LABELS_ROTATION_LON_TYPE = Double.class;

    public static final String PROPERTY_LABELS_ROTATION_LAT_NAME = "graticule.labels.rotation.lat";
    public static final String PROPERTY_LABELS_ROTATION_LAT_LABEL = "Rotation (Latitude)";
    public static final String PROPERTY_LABELS_ROTATION_LAT_TOOLTIP = "Rotate latitude labels (0 degrees = perpendicular)";
    private static final String PROPERTY_LABELS_ROTATION_LAT_ALIAS = "labelsRotationLat";
    public static final double PROPERTY_LABELS_ROTATION_LAT_DEFAULT = 90;
    public static final Class PROPERTY_LABELS_ROTATION_LAT_TYPE = Double.class;




    // Property Settings: Gridlines Section

    public static final String PROPERTY_GRIDLINES_SECTION_NAME = "graticule.gridlines.section";
    public static final String PROPERTY_GRIDLINES_SECTION_LABEL = "Gridlines";
    public static final String PROPERTY_GRIDLINES_SECTION_TOOLTIP = "Configuration options for the gridlines";
    public static final String PROPERTY_GRIDLINES_SECTION_ALIAS = "graticuleGridlinesSection";

    public static final String PROPERTY_GRIDLINES_SHOW_NAME = "graticule.gridlines.show";
    public static final String PROPERTY_GRIDLINES_SHOW_LABEL = "Show";
    public static final String PROPERTY_GRIDLINES_SHOW_TOOLTIP = "Display gridlines";
    private static final String PROPERTY_GRIDLINES_SHOW_ALIAS = "graticuleGridlinesShow";
    public static final boolean PROPERTY_GRIDLINES_SHOW_DEFAULT = true;
    public static final Class PROPERTY_GRIDLINES_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_GRIDLINES_WIDTH_NAME = "graticule.gridlines.width";
    public static final String PROPERTY_GRIDLINES_WIDTH_LABEL = "Width";
    public static final String PROPERTY_GRIDLINES_WIDTH_TOOLTIP = "Set width of gridlines";
    private static final String PROPERTY_GRIDLINES_WIDTH_ALIAS = "gridlinesWidth";
    public static final double PROPERTY_GRIDLINES_WIDTH_DEFAULT = 1.5;
    public static final Class PROPERTY_GRIDLINES_WIDTH_TYPE = Double.class;

    public static final String PROPERTY_GRIDLINES_DASHED_PHASE_NAME = "graticule.gridlines.dashed.phase";
    public static final String PROPERTY_GRIDLINES_DASHED_PHASE_LABEL = "Dash Length";
    public static final String PROPERTY_GRIDLINES_DASHED_PHASE_TOOLTIP = "Set dash length of gridlines or solid gridlines (0=SOLID)";
    private static final String PROPERTY_GRIDLINES_DASHED_PHASE_ALIAS = "graticuleGridlinesDashedPhase";
    public static final double PROPERTY_GRIDLINES_DASHED_PHASE_DEFAULT = 6;
    public static final Class PROPERTY_GRIDLINES_DASHED_PHASE_TYPE = Double.class;

    public static final String PROPERTY_GRIDLINES_TRANSPARENCY_NAME = "graticule.gridlines.transparency";
    public static final String PROPERTY_GRIDLINES_TRANSPARENCY_LABEL = "Transparency";
    public static final String PROPERTY_GRIDLINES_TRANSPARENCY_TOOLTIP = "Set transparency of gridlines";
    private static final String PROPERTY_GRIDLINES_TRANSPARENCY_ALIAS = "gridlinesTransparency";
    public static final double PROPERTY_GRIDLINES_TRANSPARENCY_DEFAULT = 0.6;
    public static final Class PROPERTY_GRIDLINES_TRANSPARENCY_TYPE = Double.class;

    public static final String PROPERTY_GRIDLINES_COLOR_NAME = "graticule.gridlines.color";
    public static final String PROPERTY_GRIDLINES_COLOR_LABEL = "Color";
    public static final String PROPERTY_GRIDLINES_COLOR_TOOLTIP = "Set color of gridlines";
    private static final String PROPERTY_GRIDLINES_COLOR_ALIAS = "gridlinesColor";
    public static final Color PROPERTY_GRIDLINES_COLOR_DEFAULT = new Color(0, 0, 80);
    public static final Class PROPERTY_GRIDLINES_COLOR_TYPE = Color.class;


    // Property Settings: Border Section

    public static final String PROPERTY_BORDER_SECTION_NAME = "graticule.border.section";
    public static final String PROPERTY_BORDER_SECTION_ALIAS = "graticuleBorderSection";
    public static final String PROPERTY_BORDER_SECTION_LABEL = "Border";
    public static final String PROPERTY_BORDER_SECTION_TOOLTIP = "Configuration options for adding a border around the data image";

    public static final String PROPERTY_BORDER_SHOW_NAME = "graticule.border.show";
    public static final String PROPERTY_BORDER_SHOW_LABEL = "Show";
    public static final String PROPERTY_BORDER_SHOW_TOOLTIP = "Display a border around the data image";
    private static final String PROPERTY_BORDER_SHOW_ALIAS = "graticuleBorderShow";
    public static final boolean PROPERTY_BORDER_SHOW_DEFAULT = true;
    public static final Class PROPERTY_BORDER_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_BORDER_WIDTH_NAME = "graticule.border.width";
    public static final String PROPERTY_BORDER_WIDTH_LABEL = "Width";
    public static final String PROPERTY_BORDER_WIDTH_TOOLTIP = "Width of border line";
    private static final String PROPERTY_BORDER_WIDTH_ALIAS = "graticuleBorderWidth";
    public static final double PROPERTY_BORDER_WIDTH_DEFAULT = 2;
    public static final Class PROPERTY_BORDER_WIDTH_TYPE = Double.class;

    public static final String PROPERTY_BORDER_COLOR_NAME = "graticule.border.color";
    public static final String PROPERTY_BORDER_COLOR_LABEL = "Color";
    public static final String PROPERTY_BORDER_COLOR_TOOLTIP = "Color of border line";
    private static final String PROPERTY_BORDER_COLOR_ALIAS = "graticuleBorderColor";
    public static final Color PROPERTY_BORDER_COLOR_DEFAULT = Color.BLACK;
    public static final Class PROPERTY_BORDER_COLOR_TYPE = Color.class;


    // Property Settings: Tickmarks Section

    public static final String PROPERTY_TICKMARKS_SECTION_NAME = "graticule.tickmarks.section";
    public static final String PROPERTY_TICKMARKS_SECTION_ALIAS = "graticuleTickmarksSection";
    public static final String PROPERTY_TICKMARKS_SECTION_LABEL = "Tickmarks";
    public static final String PROPERTY_TICKMARKS_SECTION_TOOLTIP = "Configuration options for adding tickmarks around the data image";

    public static final String PROPERTY_TICKMARKS_SHOW_NAME = "graticule.tickmarks.show";
    public static final String PROPERTY_TICKMARKS_SHOW_LABEL = "Show";
    public static final String PROPERTY_TICKMARKS_SHOW_TOOLTIP = "Display tickmarks";
    public static final String PROPERTY_TICKMARKS_SHOW_ALIAS = "graticuleTickmarksShow";
    public static final boolean PROPERTY_TICKMARKS_SHOW_DEFAULT = true;
    public static final Class PROPERTY_TICKMARKS_SHOW_TYPE = Boolean.class;

    public static final String PROPERTY_TICKMARKS_INSIDE_NAME = "graticule.tickmarks.inside";
    public static final String PROPERTY_TICKMARKS_INSIDE_LABEL = "Put Inside";
    public static final String PROPERTY_TICKMARKS_INSIDE_TOOLTIP = "Put tickmarks on inside of data image";
    public static final String PROPERTY_TICKMARKS_INSIDE_ALIAS = "graticuleTickmarksInside";
    public static final boolean PROPERTY_TICKMARKS_INSIDE_DEFAULT = false;
    public static final Class PROPERTY_TICKMARKS_INSIDE_TYPE = Boolean.class;

    public static final String PROPERTY_TICKMARKS_LENGTH_NAME = "graticule.tickmarks.length";
    public static final String PROPERTY_TICKMARKS_LENGTH_LABEL = "Length";
    public static final String PROPERTY_TICKMARKS_LENGTH_TOOLTIP = "Set length of tickmarks";
    public static final String PROPERTY_TICKMARKS_LENGTH_ALIAS = "graticuleTickmarksLength";
    public static final double PROPERTY_TICKMARKS_LENGTH_DEFAULT = 6.0;
    public static final Class PROPERTY_TICKMARKS_LENGTH_TYPE = Double.class;

    public static final String PROPERTY_TICKMARKS_COLOR_NAME = "graticule.tickmarks.color";
    public static final String PROPERTY_TICKMARKS_COLOR_LABEL = "Color";
    public static final String PROPERTY_TICKMARKS_COLOR_TOOLTIP = "Set color of the tickmarks";
    private static final String PROPERTY_TICKMARKS_COLOR_ALIAS = "graticuleTickmarksColor";
    public static final Color PROPERTY_TICKMARKS_COLOR_DEFAULT = Color.BLACK;
    public static final Class PROPERTY_TICKMARKS_COLOR_TYPE = Color.class;


    // Property Settings: Corner Labels Section

    public static final String PROPERTY_CORNER_LABELS_SECTION_NAME = "graticule.corner.labels.section";
    public static final String PROPERTY_CORNER_LABELS_SECTION_ALIAS = "graticuleCornerLabelsSection";
    public static final String PROPERTY_CORNER_LABELS_SECTION_LABEL = "Corner Labels";
    public static final String PROPERTY_CORNER_LABELS_SECTION_TOOLTIP = "Configuration options for labels placed at the corners of the image";

    public static final String PROPERTY_CORNER_LABELS_NORTH_NAME = "graticule.corner.labels.north";
    public static final String PROPERTY_CORNER_LABELS_NORTH_LABEL = "North";
    public static final String PROPERTY_CORNER_LABELS_NORTH_TOOLTIP = "Display north corner labels";
    public static final String PROPERTY_CORNER_LABELS_NORTH_ALIAS = "graticuleCornerLabelsNorth";
    public static final boolean PROPERTY_CORNER_LABELS_NORTH_DEFAULT = false;
    public static final Class PROPERTY_CORNER_LABELS_NORTH_TYPE = Boolean.class;

    public static final String PROPERTY_CORNER_LABELS_WEST_NAME = "graticule.corner.labels.west";
    public static final String PROPERTY_CORNER_LABELS_WEST_LABEL = "West";
    public static final String PROPERTY_CORNER_LABELS_WEST_TOOLTIP = "Display west corner labels";
    public static final String PROPERTY_CORNER_LABELS_WEST_ALIAS = "graticuleCornerLabelsWest";
    public static final boolean PROPERTY_CORNER_LABELS_WEST_DEFAULT = false;
    public static final Class PROPERTY_CORNER_LABELS_WEST_TYPE = Boolean.class;

    public static final String PROPERTY_CORNER_LABELS_EAST_NAME = "graticule.corner.labels.east";
    public static final String PROPERTY_CORNER_LABELS_EAST_LABEL = "East";
    public static final String PROPERTY_CORNER_LABELS_EAST_TOOLTIP = "Display east corner labels";
    public static final String PROPERTY_CORNER_LABELS_EAST_ALIAS = "graticuleCornerLabelsEast";
    public static final boolean PROPERTY_CORNER_LABELS_EAST_DEFAULT = false;
    public static final Class PROPERTY_CORNER_LABELS_EAST_TYPE = Boolean.class;

    public static final String PROPERTY_CORNER_LABELS_SOUTH_NAME = "graticule.corner.labels.south";
    public static final String PROPERTY_CORNER_LABELS_SOUTH_LABEL = "South";
    public static final String PROPERTY_CORNER_LABELS_SOUTH_TOOLTIP = "Display south corner labels";
    public static final String PROPERTY_CORNER_LABELS_SOUTH_ALIAS = "graticuleCornerLabelsSouth";
    public static final boolean PROPERTY_CORNER_LABELS_SOUTH_DEFAULT = false;
    public static final Class PROPERTY_CORNER_LABELS_SOUTH_TYPE = Boolean.class;


    // Property Settings: Inside Labels Backdrop Section

    public static final String PROPERTY_INSIDE_LABELS_SECTION_NAME = "graticule.labels.backdrop.section";
    public static final String PROPERTY_INSIDE_LABELS_SECTION_ALIAS = "graticuleLabelsBackdropSection";
    public static final String PROPERTY_INSIDE_LABELS_SECTION_LABEL = "Inside Labels Backdrop";
    public static final String PROPERTY_INSIDE_LABELS_SECTION_TOOLTIP = "Configuration options for backdrop of labels placed on the inside of the image";

    public static final String PROPERTY_INSIDE_LABELS_BG_COLOR_NAME = "graticule.text.bg.color";
    public static final String PROPERTY_INSIDE_LABELS_BG_COLOR_LABEL = "Color";
    public static final String PROPERTY_INSIDE_LABELS_BG_COLOR_TOOLTIP = "Set color of the backdrop of the inside labels";
    private static final String PROPERTY_INSIDE_LABELS_BG_COLOR_ALIAS = "textBgColor";
    public static final Color PROPERTY_INSIDE_LABELS_BG_COLOR_DEFAULT = Color.WHITE;
    public static final Class PROPERTY_INSIDE_LABELS_BG_COLOR_TYPE = Color.class;

    public static final String PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_NAME = "graticule.text.bg.transparency";
    public static final String PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_LABEL = "Transparency";
    public static final String PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_TOOLTIP = "Set transparency of the backdrop of the inside labels";
    private static final String PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_ALIAS = "textBgTransparency";
    public static final double PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_DEFAULT = 0.3;
    public static final Class PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_TYPE = Double.class;

    // ---------------------------------------------------------

    public static final String PROPERTY_NAME_RASTER = "raster";


    public static final String PROPERTY_NUM_GRID_LINES_NAME = "graticule.num.grid.lines";
    public static final int PROPERTY_NUM_GRID_LINES_DEFAULT = 4;
    public static final String PROPERTY_NUM_GRID_LINES_LABEL = "Number of Gridlines (auto-generate)";
    public static final String PROPERTY_NUM_GRID_LINES_TOOLTIP = "<html>Number of gridlines (approximate due to rounding) <br>to auto-generate if lat or lon spacing = 0</html>";
    public static final String PROPERTY_NUM_GRID_LINES_ALIAS = "numGridLines";
    public static final Class PROPERTY_NUM_GRID_LINES_TYPE = Integer.class;

    public static final String PROPERTY_MINOR_STEPS_NAME = "graticule.minor.steps";
    public static final int PROPERTY_MINOR_STEPS_DEFAULT = 10;
    public static final String PROPERTY_MINOR_STEPS_LABEL = "Minor Steps Between Gridlines";
    public static final String PROPERTY_MINOR_STEPS_TOOLTIP = "Number of minor steps between gridlines";
    public static final String PROPERTY_MINOR_STEPS_ALIAS = "minorSteps";
    public static final Class PROPERTY_MINOR_STEPS_TYPE = Integer.class;


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

        final Property filenameModel = Property.create(PROPERTY_FILE_NAME_KEY, PROPERTY_FILE_NAME_TYPE, true, true);
        filenameModel.getDescriptor().setAlias(PROPERTY_FILE_NAME_ALIAS);
        vc.addProperty(filenameModel);

        final Property headerModel = Property.create(PROPERTY_HEADER_TEXTFIELD_KEY, PROPERTY_HEADER_TEXTFIELD_TYPE, true, true);
        headerModel.getDescriptor().setAlias(PROPERTY_HEADER_TEXTFIELD_ALIAS);
        vc.addProperty(headerModel);

        final Property header2Model = Property.create(PROPERTY_HEADER2_TEXTFIELD_KEY, PROPERTY_HEADER2_TEXTFIELD_TYPE, true, true);
        header2Model.getDescriptor().setAlias(PROPERTY_HEADER2_TEXTFIELD_ALIAS);
        vc.addProperty(header2Model);

        final Property footerModel = Property.create(PROPERTY_FOOTER_TEXTFIELD_KEY, PROPERTY_FOOTER_TEXTFIELD_TYPE, true, true);
        footerModel.getDescriptor().setAlias(PROPERTY_FOOTER_TEXTFIELD_ALIAS);
        vc.addProperty(footerModel);

        final Property footerMetadataModel = Property.create(PROPERTY_FOOTER_METADATA_KEY, PROPERTY_FOOTER_METADATA_TYPE, true, true);
        footerMetadataModel.getDescriptor().setAlias(PROPERTY_FOOTER_METADATA_ALIAS);
        vc.addProperty(footerMetadataModel);


        // Location Section

        final Property locationSectionModel = Property.create(PROPERTY_LOCATION_SECTION_KEY, Boolean.class, true, true);
        locationSectionModel.getDescriptor().setAlias(PROPERTY_LOCATION_SECTION_ALIAS);
        vc.addProperty(locationSectionModel);

        final Property locationModel = Property.create(PROPERTY_LOCATION_KEY, PROPERTY_LOCATION_TYPE, true, true);
        locationModel.getDescriptor().setAlias(PROPERTY_LOCATION_ALIAS);
        vc.addProperty(locationModel);

        final Property footerLocationModel = Property.create(PROPERTY_FOOTER_LOCATION_KEY, PROPERTY_FOOTER_LOCATION_TYPE, true, true);
        footerLocationModel.getDescriptor().setAlias(PROPERTY_FOOTER_LOCATION_ALIAS);
        vc.addProperty(footerLocationModel);

        final Property locationGapFactorModel = Property.create(PROPERTY_LOCATION_GAP_FACTOR_KEY, PROPERTY_LOCATION_GAP_FACTOR_TYPE, true, true);
        locationGapFactorModel.getDescriptor().setAlias(PROPERTY_LOCATION_GAP_FACTOR_ALIAS);
        vc.addProperty(locationGapFactorModel);



        // Header Formatting Section

        final Property headerFormatSectionModel = Property.create(PROPERTY_HEADER_FORMAT_SECTION_KEY, Boolean.class, true, true);
        headerFormatSectionModel.getDescriptor().setAlias(PROPERTY_HEADER_FORMAT_SECTION_ALIAS);
        vc.addProperty(headerFormatSectionModel);






        final Property rasterModel = Property.create(PROPERTY_NAME_RASTER, RasterDataNode.class);
        rasterModel.getDescriptor().setNotNull(true);
        vc.addProperty(rasterModel);

        final Property transformModel = Property.create(PROPERTY_NAME_TRANSFORM, new AffineTransform());
        transformModel.getDescriptor().setTransient(true);
        vc.addProperty(transformModel);


        // Grid Spacing Section

        final Property gridSpacingSectionModel = Property.create(PROPERTY_GRID_SPACING_SECTION_NAME, Boolean.class, true, true);
        gridSpacingSectionModel.getDescriptor().setAlias(PROPERTY_GRID_SPACING_SECTION_ALIAS);
        vc.addProperty(gridSpacingSectionModel);

        // hidden from user
        final Property resPixelsModel = Property.create(PROPERTY_NUM_GRID_LINES_NAME, Integer.class, PROPERTY_NUM_GRID_LINES_DEFAULT, true);
        resPixelsModel.getDescriptor().setAlias(PROPERTY_NUM_GRID_LINES_ALIAS);
        vc.addProperty(resPixelsModel);

        final Property minorStepsModel = Property.create(PROPERTY_MINOR_STEPS_NAME, Integer.class, PROPERTY_MINOR_STEPS_DEFAULT, true);
        minorStepsModel.getDescriptor().setAlias(PROPERTY_MINOR_STEPS_ALIAS);
        vc.addProperty(minorStepsModel);

        final Property gridSpacingLatModel = Property.create(PROPERTY_GRID_SPACING_LAT_NAME, PROPERTY_GRID_SPACING_LAT_TYPE, PROPERTY_GRID_SPACING_LAT_DEFAULT, true);
        gridSpacingLatModel.getDescriptor().setAlias(PROPERTY_GRID_SPACING_LAT_ALIAS);
        vc.addProperty(gridSpacingLatModel);

        final Property gridSpacingLonModel = Property.create(PROPERTY_GRID_SPACING_LON_NAME, PROPERTY_GRID_SPACING_LON_TYPE, PROPERTY_GRID_SPACING_LON_DEFAULT, true);
        gridSpacingLonModel.getDescriptor().setAlias(PROPERTY_GRID_SPACING_LON_ALIAS);
        vc.addProperty(gridSpacingLonModel);




        // Labels Section

        final Property labelsSectionModel = Property.create(PROPERTY_LABELS_SECTION_NAME, Boolean.class, true, true);
        labelsSectionModel.getDescriptor().setAlias(PROPERTY_LABELS_SECTION_ALIAS);
        vc.addProperty(labelsSectionModel);





        final Property insideLabelsSectionModel = Property.create(PROPERTY_INSIDE_LABELS_SECTION_NAME, Boolean.class, true, true);
        insideLabelsSectionModel.getDescriptor().setAlias(PROPERTY_INSIDE_LABELS_SECTION_ALIAS);
        vc.addProperty(insideLabelsSectionModel);

        final Property cornerLabelsSectionModel = Property.create(PROPERTY_CORNER_LABELS_SECTION_NAME, Boolean.class, true, true);
        cornerLabelsSectionModel.getDescriptor().setAlias(PROPERTY_CORNER_LABELS_SECTION_ALIAS);
        vc.addProperty(cornerLabelsSectionModel);


        final Property borderSectionModel = Property.create(PROPERTY_BORDER_SECTION_NAME, Boolean.class, true, true);
        borderSectionModel.getDescriptor().setAlias(PROPERTY_BORDER_SECTION_ALIAS);
        vc.addProperty(borderSectionModel);

        final Property gridlinesSectionModel = Property.create(PROPERTY_GRIDLINES_SECTION_NAME, Boolean.class, true, true);
        gridlinesSectionModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_SECTION_ALIAS);
        vc.addProperty(gridlinesSectionModel);




        final Property lineColorModel = Property.create(PROPERTY_GRIDLINES_COLOR_NAME, Color.class, PROPERTY_GRIDLINES_COLOR_DEFAULT, true);
        lineColorModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_COLOR_ALIAS);
        vc.addProperty(lineColorModel);

        final Property lineTransparencyModel = Property.create(PROPERTY_GRIDLINES_TRANSPARENCY_NAME, Double.class, PROPERTY_GRIDLINES_TRANSPARENCY_DEFAULT, true);
        lineTransparencyModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_TRANSPARENCY_ALIAS);
        vc.addProperty(lineTransparencyModel);

        final Property lineWidthModel = Property.create(PROPERTY_GRIDLINES_WIDTH_NAME, Double.class, PROPERTY_GRIDLINES_WIDTH_DEFAULT, true);
        lineWidthModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_WIDTH_ALIAS);
        vc.addProperty(lineWidthModel);


        final Property textFgColorModel = Property.create(PROPERTY_LABELS_COLOR_NAME, Color.class, PROPERTY_LABELS_COLOR_DEFAULT, true);
        textFgColorModel.getDescriptor().setAlias(PROPERTY_LABELS_COLOR_ALIAS);
        vc.addProperty(textFgColorModel);



        final Property textBgColorModel = Property.create(PROPERTY_INSIDE_LABELS_BG_COLOR_NAME, Color.class, PROPERTY_INSIDE_LABELS_BG_COLOR_DEFAULT, true);
        textBgColorModel.getDescriptor().setAlias(PROPERTY_INSIDE_LABELS_BG_COLOR_ALIAS);
        vc.addProperty(textBgColorModel);

        final Property textBgTransparencyModel = Property.create(PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_NAME, Double.class, PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_DEFAULT, true);
        textBgTransparencyModel.getDescriptor().setAlias(PROPERTY_INSIDE_LABELS_BG_TRANSPARENCY_ALIAS);
        vc.addProperty(textBgTransparencyModel);


        final Property textFontSizeModel = Property.create(PROPERTY_LABELS_SIZE_NAME, Integer.class, PROPERTY_LABELS_SIZE_DEFAULT, true);
        textFontSizeModel.getDescriptor().setAlias(PROPERTY_LABELS_SIZE_ALIAS);
        vc.addProperty(textFontSizeModel);


        final Property textFontItalicModel = Property.create(PROPERTY_LABELS_ITALIC_NAME, Boolean.class, PROPERTY_LABELS_ITALIC_DEFAULT, true);
        textFontItalicModel.getDescriptor().setAlias(PROPERTY_LABELS_ITALIC_ALIAS);
        vc.addProperty(textFontItalicModel);

        final Property textFontBoldModel = Property.create(PROPERTY_LABELS_BOLD_NAME, Boolean.class, PROPERTY_LABELS_BOLD_DEFAULT, true);
        textFontBoldModel.getDescriptor().setAlias(PROPERTY_LABELS_BOLD_ALIAS);
        vc.addProperty(textFontBoldModel);

        final Property textFontModel = Property.create(PROPERTY_LABELS_FONT_NAME, String.class, PROPERTY_LABELS_FONT_DEFAULT, true);
        textFontModel.getDescriptor().setAlias(PROPERTY_LABELS_FONT_ALIAS);
        vc.addProperty(textFontModel);


        final Property textOutsideModel = Property.create(PROPERTY_LABELS_INSIDE_NAME, Boolean.class, PROPERTY_LABELS_INSIDE_DEFAULT, true);
        textOutsideModel.getDescriptor().setAlias(PROPERTY_LABELS_INSIDE_ALIAS);
        vc.addProperty(textOutsideModel);

        final Property textRotationNorthModel = Property.create(PROPERTY_LABELS_ROTATION_LON_NAME, Double.class, PROPERTY_LABELS_ROTATION_LON_DEFAULT, true);
        textRotationNorthModel.getDescriptor().setAlias(PROPERTY_LABELS_ROTATION_LON_ALIAS);
        vc.addProperty(textRotationNorthModel);

        final Property textRotationWestModel = Property.create(PROPERTY_LABELS_ROTATION_LAT_NAME, Double.class, PROPERTY_LABELS_ROTATION_LAT_DEFAULT, true);
        textRotationWestModel.getDescriptor().setAlias(PROPERTY_LABELS_ROTATION_LAT_ALIAS);
        vc.addProperty(textRotationWestModel);


        final Property textEnabledNorthModel = Property.create(PROPERTY_LABELS_NORTH_NAME, Boolean.class, PROPERTY_LABELS_NORTH_DEFAULT, true);
        textEnabledNorthModel.getDescriptor().setAlias(PROPERTY_LABELS_NORTH_ALIAS);
        vc.addProperty(textEnabledNorthModel);

        final Property textEnabledSouthModel = Property.create(PROPERTY_LABELS_SOUTH_NAME, Boolean.class, PROPERTY_LABELS_SOUTH_DEFAULT, true);
        textEnabledSouthModel.getDescriptor().setAlias(PROPERTY_LABELS_SOUTH_ALIAS);
        vc.addProperty(textEnabledSouthModel);

        final Property textEnabledWestModel = Property.create(PROPERTY_LABELS_WEST_NAME, Boolean.class, PROPERTY_LABELS_WEST_DEFAULT, true);
        textEnabledWestModel.getDescriptor().setAlias(PROPERTY_LABELS_WEST_ALIAS);
        vc.addProperty(textEnabledWestModel);

        final Property textEnabledEastModel = Property.create(PROPERTY_LABELS_EAST_NAME, Boolean.class, PROPERTY_LABELS_EAST_DEFAULT, true);
        textEnabledEastModel.getDescriptor().setAlias(PROPERTY_LABELS_EAST_ALIAS);
        vc.addProperty(textEnabledEastModel);

        final Property lineEnabledModel = Property.create(PROPERTY_GRIDLINES_SHOW_NAME, Boolean.class, PROPERTY_GRIDLINES_SHOW_DEFAULT, true);
        lineEnabledModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_SHOW_ALIAS);
        vc.addProperty(lineEnabledModel);


        final Property lineDashedPhaseModel = Property.create(PROPERTY_GRIDLINES_DASHED_PHASE_NAME, Double.class, PROPERTY_GRIDLINES_DASHED_PHASE_DEFAULT, true);
        lineDashedPhaseModel.getDescriptor().setAlias(PROPERTY_GRIDLINES_DASHED_PHASE_ALIAS);
        vc.addProperty(lineDashedPhaseModel);

        final Property borderEnabledModel = Property.create(PROPERTY_BORDER_SHOW_NAME, Boolean.class, PROPERTY_BORDER_SHOW_DEFAULT, true);
        borderEnabledModel.getDescriptor().setAlias(PROPERTY_BORDER_SHOW_ALIAS);
        vc.addProperty(borderEnabledModel);

        final Property formatCompassModel = Property.create(PROPERTY_LABELS_SUFFIX_NSWE_NAME, Boolean.class, PROPERTY_LABELS_SUFFIX_NSWE_DEFAULT, false);
        formatCompassModel.getDescriptor().setAlias(PROPERTY_LABELS_SUFFIX_NSWE_ALIAS);
        vc.addProperty(formatCompassModel);

        final Property formatDecimalModel = Property.create(PROPERTY_LABELS_DECIMAL_VALUE_NAME, Boolean.class, PROPERTY_LABELS_DECIMAL_VALUE_DEFAULT, false);
        formatDecimalModel.getDescriptor().setAlias(PROPERTY_LABELS_DECIMAL_VALUE_ALIAS);
        vc.addProperty(formatDecimalModel);


        final Property borderColorModel = Property.create(PROPERTY_BORDER_COLOR_NAME, Color.class, PROPERTY_BORDER_COLOR_DEFAULT, true);
        borderColorModel.getDescriptor().setAlias(PROPERTY_BORDER_COLOR_ALIAS);
        vc.addProperty(borderColorModel);

        final Property borderWidthModel = Property.create(PROPERTY_BORDER_WIDTH_NAME, Double.class, PROPERTY_BORDER_WIDTH_DEFAULT, true);
        borderWidthModel.getDescriptor().setAlias(PROPERTY_BORDER_WIDTH_ALIAS);
        vc.addProperty(borderWidthModel);


        final Property textCornerTopLeftLonEnabledModel = Property.create(PROPERTY_CORNER_LABELS_NORTH_NAME, Boolean.class, PROPERTY_CORNER_LABELS_NORTH_DEFAULT, true);
        textCornerTopLeftLonEnabledModel.getDescriptor().setAlias(PROPERTY_CORNER_LABELS_NORTH_ALIAS);
        vc.addProperty(textCornerTopLeftLonEnabledModel);

        final Property textCornerTopLeftLatEnabledModel = Property.create(PROPERTY_CORNER_LABELS_WEST_NAME, Boolean.class, PROPERTY_CORNER_LABELS_WEST_DEFAULT, true);
        textCornerTopLeftLatEnabledModel.getDescriptor().setAlias(PROPERTY_CORNER_LABELS_WEST_ALIAS);
        vc.addProperty(textCornerTopLeftLatEnabledModel);


        final Property textCornerTopRightLatEnabledModel = Property.create(PROPERTY_CORNER_LABELS_EAST_NAME, Boolean.class, PROPERTY_CORNER_LABELS_EAST_DEFAULT, true);
        textCornerTopRightLatEnabledModel.getDescriptor().setAlias(PROPERTY_CORNER_LABELS_EAST_ALIAS);
        vc.addProperty(textCornerTopRightLatEnabledModel);


        final Property textCornerBottomLeftLonEnabledModel = Property.create(PROPERTY_CORNER_LABELS_SOUTH_NAME, Boolean.class, PROPERTY_CORNER_LABELS_SOUTH_DEFAULT, true);
        textCornerBottomLeftLonEnabledModel.getDescriptor().setAlias(PROPERTY_CORNER_LABELS_SOUTH_ALIAS);
        vc.addProperty(textCornerBottomLeftLonEnabledModel);


        // Tickmarks Section

        final Property tickmarksSectionModel = Property.create(PROPERTY_TICKMARKS_SECTION_NAME, Boolean.class, true, true);
        tickmarksSectionModel.getDescriptor().setAlias(PROPERTY_TICKMARKS_SECTION_ALIAS);
        vc.addProperty(tickmarksSectionModel);

        final Property tickMarkEnabledModel = Property.create(PROPERTY_TICKMARKS_SHOW_NAME, PROPERTY_TICKMARKS_SHOW_TYPE, PROPERTY_TICKMARKS_SHOW_DEFAULT, true);
        tickMarkEnabledModel.getDescriptor().setAlias(PROPERTY_TICKMARKS_SHOW_ALIAS);
        vc.addProperty(tickMarkEnabledModel);

        final Property tickMarkInsideModel = Property.create(PROPERTY_TICKMARKS_INSIDE_NAME, PROPERTY_TICKMARKS_INSIDE_TYPE, PROPERTY_TICKMARKS_INSIDE_DEFAULT, true);
        tickMarkInsideModel.getDescriptor().setAlias(PROPERTY_TICKMARKS_INSIDE_ALIAS);
        vc.addProperty(tickMarkInsideModel);

        final Property tickMarkLengthModel = Property.create(PROPERTY_TICKMARKS_LENGTH_NAME, PROPERTY_TICKMARKS_LENGTH_TYPE, PROPERTY_TICKMARKS_LENGTH_DEFAULT, true);
        tickMarkLengthModel.getDescriptor().setAlias(PROPERTY_TICKMARKS_LENGTH_ALIAS);
        vc.addProperty(tickMarkLengthModel);


        final Property tickmarkColorModel = Property.create(PROPERTY_TICKMARKS_COLOR_NAME, PROPERTY_TICKMARKS_COLOR_TYPE, PROPERTY_TICKMARKS_COLOR_DEFAULT, true);
        tickmarkColorModel.getDescriptor().setAlias(PROPERTY_TICKMARKS_COLOR_ALIAS);
        vc.addProperty(tickmarkColorModel);



        return vc;
    }
}
