/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.tesla.ops.api.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.protobuf.DescriptorProtos.FileDescriptorSet;

import io.github.tesla.common.proto.ProtocInvoker;
import io.github.tesla.common.proto.ProtocInvoker.ProtocInvocationException;
import io.github.tesla.ops.api.service.ProtobufService;
import io.github.tesla.ops.common.TeslaException;

/**
 * @author liushiming
 * @version ProtobufFileServiceImpl.java, v 0.0.1 2018年1月8日 下午4:16:18 liushiming
 */
@Service
public class ProtobufServiceImpl implements ProtobufService {

  private String protoFileDirectory;

  @PostConstruct
  public void init() {
    try {
      Path protosTempDirectory = Files.createTempDirectory("protos");
      protoFileDirectory = protosTempDirectory.toFile().getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public byte[] compileDirectoryProto(MultipartFile directoryZipStream) {
    String fileDirectory = null;
    try {
      fileDirectory = this.uploadZipFile(directoryZipStream);
      ProtocInvoker protocInvoke = ProtocInvoker.forConfig(Paths.get(fileDirectory));
      FileDescriptorSet fileDescSet = protocInvoke.invoke();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      fileDescSet.writeTo(outputStream);
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new TeslaException(e.getMessage(), e);
    } catch (ProtocInvocationException e) {
      throw new TeslaException(e.getMessage(), e);
    } finally {
      if (fileDirectory != null) {
        FileUtils.deleteQuietly(new File(fileDirectory));
      }
    }
  }


  private String uploadZipFile(MultipartFile zipFile) throws IOException {
    Path zipFileDistPath = Paths.get(protoFileDirectory, zipFile.getOriginalFilename());
    File zipFileDist = zipFileDistPath.toFile();
    FileUtils.forceMkdirParent(zipFileDist);
    zipFile.transferTo(zipFileDist);
    ZipInputStream zipInputStream = null;
    try {
      zipInputStream = new ZipInputStream(new FileInputStream(zipFileDist));
      ZipEntry zipEntry;
      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        String zipEntryName = zipEntry.getName();
        String outPath = zipFileDistPath.getParent() + "/" + zipEntryName;
        File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
        if (!file.exists()) {
          file.mkdirs();
        }
        if (new File(outPath).isDirectory()) {
          continue;
        }
        OutputStream outputStream = new FileOutputStream(outPath);
        byte[] bytes = new byte[4096];
        int len;
        while ((len = zipInputStream.read(bytes)) > 0) {
          outputStream.write(bytes, 0, len);
        }
        outputStream.close();
        zipInputStream.closeEntry();
      }
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        if (zipInputStream != null)
          zipInputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return zipFileDist.getParent();
  }


  public static void main(String[] args) throws ProtocInvocationException {
    ProtocInvoker protocInvoke = ProtocInvoker.forConfig(
        Paths.get("/var/folders/rm/b08p6hss1_1524t87fv4wp8r0000gn/T/protos7255550061228174049"));
    FileDescriptorSet set = protocInvoke.invoke();
    System.out.println(set);
  }
}
