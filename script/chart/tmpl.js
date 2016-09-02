var bubu ={{ {0} }};
var title = "{1}";
var unit = "{2}";
function getColor(d) {{
    return d > {10} ? '#800026' :
           d > {9}  ? '#BD0026' :
           d > {8}  ? '#E31A1C' :
           d > {7}  ? '#FC4E2A' :
           d > {6}   ? '#FD8D3C' :
           d > {5}  ? '#FEB24C' :
           d > {4}   ? '#FED976' :
           d > {3}   ? '#FFEDA0' :
           d < 0   ? '#000000' :
                      '#000000';
}}
var vgrades = [{3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}];