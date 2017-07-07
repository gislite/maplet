// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

final class Chunk_IEND
extends Chunk
{
    Chunk_IEND()
    {
        super(IEND);
    }

    protected boolean multipleOK()
    {
        return false;
    }
}
