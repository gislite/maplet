// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_pCAL
extends Chunk
{
    Chunk_pCAL()
    {
        super(pCAL);
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
        String purpose, unit_string;

        if ((purpose = in_data.readString()).length() > 79) {
            throw new PngExceptionSoft("pCAL purpose too long");
        }
        purpose = KeyValueChunk.repairKey(purpose);

        int X0 = in_data.readInt();
        int X1 = in_data.readInt();
        if (X1 == X0) {
            throw new PngExceptionSoft("X1 == X0 in pCAL chunk");
        }
        
        int equation_type = in_data.readUnsignedByte();
        int N = in_data.readUnsignedByte();

        if ((unit_string = in_data.readString()).length() > 79) {
            throw new PngExceptionSoft("pCAL unit string too long");
        }

        double[] P = new double[N];
        for (int i = 0; i < N; i++) {
            P[i] = in_data.readFloatingPoint();
        }

        img.data.properties.put("pixel calibration purpose", purpose);
        img.data.properties.put("pixel calibration x0", new Integer(X0));
        img.data.properties.put("pixel calibration x1", new Integer(X1));
        img.data.properties.put("pixel calibration type", new Integer(equation_type));
        img.data.properties.put("pixel calibration n", new Integer(N));
        img.data.properties.put("pixel calibration unit", unit_string);
        img.data.properties.put("pixel calibration parameters", P);
    }    
}
