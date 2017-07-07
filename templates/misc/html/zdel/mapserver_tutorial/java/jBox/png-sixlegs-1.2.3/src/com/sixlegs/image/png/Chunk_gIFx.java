// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_gIFx
extends Chunk
implements GifExtension
{
    private String identifier;
    private byte[] auth_code;
    private byte[] data;
    
    Chunk_gIFx()
    {
        super(gIFx);
    }

    protected void readData()
    throws IOException
    {
        identifier = in_data.readString(8, PngImage.ASCII_ENCODING);
        in_data.skip(8 - identifier.length());

        auth_code = new byte[3];
        in_data.readFully(auth_code);
        
        data = new byte[bytesRemaining()];
        in_data.readFully(data);

        img.data.gifExtensions.addElement(this);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public byte[] getAuthenticationCode()
    {
        return auth_code;
    }

    public byte[] getData()
    {
        return data;
    }
}
