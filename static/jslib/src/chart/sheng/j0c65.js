var bubu ={ s64:"71",s65:"517.74",s11:"34.81",s12:"23.37",s13:"202.5",s14:"58.74",s15:"180.04",s21:"142.87",s22:"100.78",s23:"291.37",s31:"120.19",s32:"558.34",s33:"210.98",s34:"232.05",s35:"196.28",s36:"234.87",s37:"219.55",s41:"209.28",s42:"258.73",s43:"324.26",s44:"462.51",s45:"310.41",s46:"46.69",s50:"77.43",s51:"213.98",s52:"98.03",s53:"150.03",s54:"36.7",s61:"81.55",s62:"122.5",s63:"31.11" };
var title = "2007年各地区供水总量";
var unit = "亿立方米";
function getColor(d) {
    return d > 310 ? '#800026' :
           d > 240  ? '#BD0026' :
           d > 210  ? '#E31A1C' :
           d > 190  ? '#FC4E2A' :
           d > 130   ? '#FD8D3C' :
           d > 80  ? '#FEB24C' :
           d > 40   ? '#FED976' :
           d > 20   ? '#FFEDA0' :
           d < 0   ? '#000000' :
                      '#000000';
}
var vgrades = [20, 40, 80, 130, 190, 210, 240, 310];