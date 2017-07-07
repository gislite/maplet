// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

final class Chunk_tIME
extends Chunk
{
    Chunk_tIME()
    {
        super(tIME);
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected void readData()
    throws IOException
    {
        TimeZone tz = TimeZone.getDefault();
        tz.setRawOffset(0);
        Calendar cal = Calendar.getInstance(tz);
        int year   = in_data.readUnsignedShort();
        int month  = in_data.readUnsignedByte();
        int day    = in_data.readUnsignedByte();
        int hour   = in_data.readUnsignedByte();
        int minute = in_data.readUnsignedByte();
        int second = in_data.readUnsignedByte();
        if (month > 12 || day > 31 || hour > 23 || minute > 59 || second > 60 ||
            month < 1 || day < 1 || hour < 0 || minute < 0 || second < 0)
            throw new PngExceptionSoft("Bad tIME data; index out of bounds");
        cal.set(year, month - 1, day, hour, minute, second);
    
        img.data.properties.put("time", cal.getTime());
    }
}
