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
package io.github.tesla.gateway.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;


/**
 * @author liushiming
 * @version PackageUtil.java, v 0.0.1 2018年5月9日 上午9:59:02 liushiming
 */
public final class ClassUtils {

  private ClassUtils() {}

  private static Logger log = LoggerFactory.getLogger(ClassUtils.class);

  public static Set<Class<?>> findAllClasses(String scanPackages) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Resource[] resources = new Resource[0];
    try {
      resources = scan(loader, scanPackages);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return convert(loader, resources);
  }

  private static Resource[] scan(ClassLoader loader, String packageName) throws IOException {
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
    String pattern = "classpath*:"
        + org.springframework.util.ClassUtils.convertClassNameToResourcePath(packageName)
        + "/**/*.class";
    return resolver.getResources(pattern);
  }

  private static Class<?> loadClass(ClassLoader loader, Resource resource) {
    try {
      CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(loader);
      MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
      return org.springframework.util.ClassUtils.forName(reader.getClassMetadata().getClassName(),
          loader);
    } catch (LinkageError | ClassNotFoundException e) {
      log.error("Ignoring candidate class resource " + resource + " due to " + e);
      return null;
    } catch (Throwable e) {
      log.error("Unexpected failure when loading class resource " + resource, e);
      return null;
    }
  }

  private static Set<Class<?>> convert(ClassLoader loader, Resource[] resources) {
    Set<Class<?>> classSet = new HashSet<>(resources.length);
    for (Resource resource : resources) {
      Class<?> clazz = loadClass(loader, resource);
      if (clazz != null) {
        classSet.add(clazz);
      }
    }
    return classSet;
  }

}
