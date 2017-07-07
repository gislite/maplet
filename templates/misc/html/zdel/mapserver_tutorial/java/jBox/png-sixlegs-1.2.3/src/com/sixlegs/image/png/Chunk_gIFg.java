// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_gIFg
extends Chunk
{
    Chunk_gIFg()
    {
        super(gIFg);
    }

    protected void readData()
    throws IOException
    {
        img.data.properties.put("gif disposal method", new Integer(in_data.readUnsignedByte()));
        img.data.properties.put("gif user input flag", new Integer(in_data.readUnsignedByte()));
        img.data.properties.put("gif delay time", new Integer(in_data.readShort()));
    }    
}
