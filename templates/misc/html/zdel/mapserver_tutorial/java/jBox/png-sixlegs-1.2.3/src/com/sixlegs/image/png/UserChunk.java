// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.IOException;

final class UserChunk
extends Chunk
{
    private ChunkHandler handler;

    UserChunk(ChunkHandler h, int type)
    {
        super(type);
        handler = h;
    }

    protected void readData()
    throws IOException
    {
        byte[] bytes = new byte[length];
        in_data.read(bytes, 0, length);
        handler.handleChunk(typeToString(type), bytes);
    }
}
