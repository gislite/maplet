// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

final class IDATInputStream
extends InputStream
{
    private static final int[] signature = { 137, 80, 78, 71, 13, 10, 26, 10 };

    private InputStream in_raw;
    private CRCInputStream in_crc;
    private ExDataInputStream in_data;

    private PngImage img;
    private Chunk cur;
    private int chunk_left;

    public IDATInputStream(PngImage img, InputStream in_raw)
    {
        this.img = img;
        this.in_raw = in_raw;
        in_crc  = new CRCInputStream(in_raw);
        in_data = new ExDataInputStream(in_crc);
    }

    // TODO: worry about non-consecutive IDAT chunks

    /* package */ void readToData()
    throws IOException
    {
        if (cur != null) return;
        for (int i = 0; i < 8; i++) 
            if (in_data.readUnsignedByte() != signature[i]) {
                throw new PngException("Improper signature");
            }
        try {
            if (getNextChunk().type != Chunk.IHDR) {
                throw new PngException("IHDR chunk must be first chunk");
            }
            while (getNextChunk().type != Chunk.IDAT);
            if (img.data.palette == null) {
                if (img.data.header.paletteUsed) {
                    throw new PngException("Required PLTE chunk not found");
                }
            }
            img.fillGammaTable();
        } catch (NullPointerException e) {
            throw new PngException("Can't find data chunk");
        }
    }

    /* package */ int count()
    {
        return in_crc.count();
    }

    private void readChunk(Chunk chunk)
    throws IOException
    {
        try {
            if (!chunk.multipleOK() && img.getChunk(chunk.type) != null) {
                String msg = "Multiple " + Chunk.typeToString(chunk.type) + " chunks are not allowed";
                if (chunk.isAncillary()) {
                    throw new PngExceptionSoft(msg);
                } else {
                    throw new PngException(msg);
                }
            }
            chunk.readData();
            img.putChunk(chunk.type, chunk);
        } catch (PngExceptionSoft e) {
            if (PngImage.allFatal) throw e;
            img.addError(e);
        } finally {
            in_data.skipBytes(chunk.bytesRemaining());
            long crc_is = in_crc.getValue();
            long crc_should_be = in_data.readUnsignedInt();
            if (crc_is != crc_should_be) {
                throw new PngException("Bad CRC value for " + Chunk.typeToString(chunk.type) + " chunk");
            }
        }
    }
  
    private Chunk getNextChunk()
    throws IOException
    {
        if (cur != null) readChunk(cur);

        try {
            chunk_left = in_data.readInt();
        } catch (EOFException e) {
            if (cur.type != Chunk.IEND) throw e;
            return null;
        }

        in_crc.reset();
        int type = in_data.readInt();

        if (chunk_left < 0) {
            throw new PngException("Bad " + Chunk.typeToString(type) + " chunk length: " + in_data.unsign(chunk_left));
        }

        cur = PngImage.getRegisteredChunk(type);
        cur.img = img;
        cur.length = chunk_left;
        cur.in_data = in_data;

        if (cur.isUnknown()) {
            String type_string = Chunk.typeToString(type);
            if (!cur.isAncillary()) {
                throw new PngException("Private critical chunk encountered: " + type_string);
            }
            for (int i = 0; i < 4; i++) {
                char c = type_string.charAt(i);
                if (c < 65 || (c > 90 && c < 97) || c > 122) {
                    throw new PngException("Corrupted chunk type: " + type_string);
                }
            }
        }
        return cur;
    }

    public int read(byte b[], int off, int len)
    throws IOException
    {
        if (cur == null)
            readToData();
        if (chunk_left == 0)
            return -1;
        int need = chunk_left < len ? chunk_left : len;
        in_data.readFully(b, off, need);
        chunk_left -= need;
        if (chunk_left == 0 && getNextChunk().type != Chunk.IDAT) {
            Chunk chunk;
            while ((chunk = getNextChunk()) != null) {
                if (chunk.beforeIDAT()) {
                    throw new PngException(Chunk.typeToString(chunk.type) + " chunk must precede first IDAT chunk");
                }
            }
            close();
        }
        return need;
    }

    private byte[] _b = new byte[1];

    public int read()
    throws IOException
    {
        return read(_b, 0, 1) > 0 ? _b[0] & 0xff : -1;
    }

    public void close()
    throws IOException
    {
        in_data.close();
    }
}
