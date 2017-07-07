// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

class Chunk_cHRM
extends Chunk
{
    protected long xwlong, ywlong, xrlong, yrlong, xglong, yglong, xblong, yblong;
    protected double xw, yw, xr, yr, xg, yg, xb, yb;
    protected double Xw, Yw, Zw;
    protected double Xr, Yr, Zr;
    protected double Xg, Yg, Zg;
    protected double Xb, Yb, Zb;

    Chunk_cHRM()
    {
        super(cHRM);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected boolean beforeIDAT()
    {
        return true;
    }

 /*
   zr = 1 - (xr + yr)
   zg = 1 - (xg + yg)
   zb = 1 - (xb + yb)
   zw = 1 - (xw + yw)

   / r \   / xr xg xb \-1 / xw / yw \
   | g | = | yr yg yb |   | yw / yw |
   \ b /   \ zr zg zb /   \ zw / yw /

   det:  xr*yg*zb + xg*yb*zr + xb*yr*zg - xg*yr*zb - xr*yb*zg - xb*yg*zr

   Yr = r * yr
   Yg = g * yg
   Yb = b * yb
 */

    protected void calc()
    {
        double zr = 1 - (xr + yr);
        double zg = 1 - (xg + yg);
        double zb = 1 - (xb + yb);
        double zw = 1 - (xw + yw);

        Xw = xw / yw;
        Yw = 1; /* yw / yw; */
        Zw = zw / yw;
    
        double det = xr*(yg*zb-zg*yb)-xg*(yr*zb-zr*yb)+xb*(yr*zg-zr*yg);
        double fr  = (Xw*(yg*zb-zg*yb)-xg*(zb-Zw*yb)+xb*(zg-Zw*yg))/det;
        double fg  = (xr*(zb-Zw*yb)-Xw*(yr*zb-zr*yb)+xb*(yr*Zw-zr))/det;
        double fb  = (xr*(yg*Zw-zg)-xg*(yr*Zw-zr)+Xw*(yr*zg-zr*yg))/det;
    
        Xr = fr * xr; Yr = fr * yr; Zr = fr * zr;
        Xg = fg * xg; Yg = fg * yg; Zg = fg * zg;
        Xb = fb * xb; Yb = fb * yb; Zb = fb * zb;

        if (img.getChunk(sRGB) == null) {
            img.data.properties.put("chromaticity xy", 
                                    new long[][]{{xwlong,ywlong},
                                                 {xrlong,yrlong},
                                                 {xglong,yglong},
                                                 {xblong,yblong}});
            img.data.properties.put("chromaticity xyz", 
                                    new double[][]{{Xw, Yw, Zw},
                                                   {Xr, Yr, Zr},
                                                   {Xg, Yg, Zg},
                                                   {Xb, Yb, Zb}});
        }
    }

    protected void readData()
    throws IOException
    {
        if (img.data.palette != null)
            throw new PngException("cHRM chunk must precede PLTE chunk");
        if (length != 32) badLength(32);
        checkRange(xw = (double)(xwlong = in_data.readUnsignedInt()) / 100000, "white");
        checkRange(yw = (double)(ywlong = in_data.readUnsignedInt()) / 100000, "white");
        checkRange(xr = (double)(xrlong = in_data.readUnsignedInt()) / 100000, "red");
        checkRange(yr = (double)(yrlong = in_data.readUnsignedInt()) / 100000, "red");
        checkRange(xg = (double)(xglong = in_data.readUnsignedInt()) / 100000, "green");
        checkRange(yg = (double)(yglong = in_data.readUnsignedInt()) / 100000, "green");
        checkRange(xb = (double)(xblong = in_data.readUnsignedInt()) / 100000, "blue");
        checkRange(yb = (double)(yblong = in_data.readUnsignedInt()) / 100000, "blue");

        calc();
    }

    private void checkRange(double value, String color)
    throws PngException
    {
        if (value < 0 || value > 0.8)
            throw new PngExceptionSoft("Invalid cHRM " + color + " point");
    }
}
