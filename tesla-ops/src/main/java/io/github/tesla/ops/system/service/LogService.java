package io.github.tesla.ops.system.service;

import org.springframework.stereotype.Service;

import io.github.tesla.ops.system.domain.LogDO;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.Query;

@Service
public interface LogService {

  PageDO<LogDO> queryList(Query query);

  int remove(Long id);

  int batchRemove(Long[] ids);
}
