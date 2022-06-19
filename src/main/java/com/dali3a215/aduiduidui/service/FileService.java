package com.dali3a215.aduiduidui.service;

import com.dali3a215.aduiduidui.entity.AduiFile;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    List<AduiFile> list(String uid, String path);

    List<AduiFile> find(String uid, String fileName);

    boolean exist(String uid, String path);

    boolean remove(String uid, String path);

    /**
     * 写入文件
     *
     * @param originIn      远程输入流
     * @param uid           用户id
     * @param path          要上传的文件的相对路径
     * @param contentLength 远程文件的长度
     * @param contentType   远程文件的类型
     * @param cover         覆盖已存在的文件
     * @return 0 写入文件成功；1 写入文件失败；2 参数不能为空；3 驱动器空间不足；4 <code>cover</code>参数为<code>false</code>
     * 且文件存在；5 文件无法覆盖；
     */
    int write(InputStream originIn, String uid, String path, long contentLength, MediaType contentType, boolean cover);

    boolean read(HttpServletResponse resp, String uid, String path);

}
