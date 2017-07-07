// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_sCAL
extends Chunk
{
    Chunk_sCAL()
    {
        super(sCAL);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected boolean beforeIDAT()
    {
        return true;
    }

    protected void readData()
    throws IOException
    {
        int unit = in_data.readUnsignedByte();
        if (unit != PngImage.UNIT_METER && unit != PngImage.UNIT_RADIAN)
            throw new PngExceptionSoft("Illegal sCAL chunk unit specifier: " + unit);

        String pixelWidthStr = in_data.readString();
        int limit = length - pixelWidthStr.length() + 2;
        try {
            double pixelWidth = in_data.parseFloatingPoint(pixelWidthStr);
            double pixelHeight = in_data.parseFloatingPoint(in_data.readString(limit));
            if (pixelWidth < 0 || pixelHeight < 0) throw new NumberFormatException();

            img.data.properties.put("pixel scale x", new Double(pixelWidth));
            img.data.properties.put("pixel scale y", new Double(pixelHeight));
            img.data.properties.put("pixel scale unit", new Integer(unit));
        } catch (NumberFormatException e) {
            throw new PngExceptionSoft("Bad floating point value in sCAL chunk");
        }
    }    
}
