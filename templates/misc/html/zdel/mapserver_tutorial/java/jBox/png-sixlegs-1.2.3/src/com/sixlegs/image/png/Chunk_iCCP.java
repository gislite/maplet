// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;

final class Chunk_iCCP
extends KeyValueChunk
{
  Chunk_iCCP() { super(iCCP); }

    protected boolean isCompressed()
    {
        return true;
    }

    protected boolean multipleOK()
    {
        return false;
    }

    protected boolean beforeIDAT()
    {
        return true;
    }

    protected String getEncoding()
    {
        return PngImage.LATIN1_ENCODING;
    }

    protected void readData()
    throws IOException
    {
        if (img.data.palette != null)
            throw new PngException("iCCP chunk must precede PLTE chunk");
        super.readData();

        img.data.properties.put("icc profile name", key);
        img.data.properties.put("icc profile", value);
    }
}
