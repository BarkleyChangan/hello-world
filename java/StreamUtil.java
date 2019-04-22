public static byte[] readBytes(InputStream in, long length) throws IOException {
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int read = 0;
    while (read < length) {
        int cur = in.read(buffer, 0, (int) Math.min(1024, length - read));
        if (cur < 0) {
            break;
        }
        read += cur;
        bo.write(buffer, 0, cur);
    }
    return bo.toByteArray();
}