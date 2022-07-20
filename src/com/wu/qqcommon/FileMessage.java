package com.wu.qqcommon;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private byte[] fileBytes;
    private int fileLen = 0;
    private String src;
    private String dest;

    public FileMessage(byte[] fileBytes, int fileLen, String src, String dest) {
        this.fileBytes = fileBytes;
        this.fileLen = fileLen;
        this.src = src;
        this.dest = dest;
    }

    public FileMessage(byte[] fileBytes, int fileLen, String src) {
        this.fileBytes = fileBytes;
        this.fileLen = fileLen;
        this.src = src;
    }

    public FileMessage(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    public FileMessage() {

    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}