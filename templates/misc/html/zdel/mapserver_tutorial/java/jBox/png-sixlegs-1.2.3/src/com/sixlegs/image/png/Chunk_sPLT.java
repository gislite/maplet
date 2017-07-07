// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.util.Hashtable;
import java.io.IOException;

final class Chunk_sPLT
extends Chunk
{
    Chunk_sPLT()
    {
        super(sPLT);
    }

    protected boolean beforeIDAT()
    {
        return true;
    }
    
    protected void readData()
    throws IOException
    {
        String name;
        if ((name = in_data.readString()).length() > 79)
            throw new PngExceptionSoft("sPLT palette name too long");
        name = KeyValueChunk.repairKey(name);
        if (img.data.palettes.containsKey(name))
            throw new PngExceptionSoft("Duplicate sPLT names");
        int depth = in_data.readUnsignedByte();
        int L = length - name.length();
        int[][] prop;
        if (depth == 8) {
            if (L % 6 != 0) badLength();
            int n = L / 6;
            prop = new int[5][n];
            for (int i = 0; i < n; i++) {
                prop[0][i] = in_data.readUnsignedByte();
                prop[1][i] = in_data.readUnsignedByte();
                prop[2][i] = in_data.readUnsignedByte();
                prop[3][i] = in_data.readUnsignedByte();
                prop[4][i] = in_data.readUnsignedShort();
            }
        } else if (depth == 16) {
            if (L % 10 != 0) badLength();
            int n = L / 10;
            prop = new int[5][n];
            for (int i = 0; i < n; i++) {
                prop[0][i] = in_data.readUnsignedShort();
                prop[1][i] = in_data.readUnsignedShort();
                prop[2][i] = in_data.readUnsignedShort();
                prop[3][i] = in_data.readUnsignedShort();
                prop[4][i] = in_data.readUnsignedShort();
            }
        } else {
            throw new PngExceptionSoft("Bad sPLT sample depth: " + depth);
        }
        img.data.palettes.put(name, prop);
    }
}
