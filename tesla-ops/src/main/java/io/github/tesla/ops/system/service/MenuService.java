package io.github.tesla.ops.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.tesla.ops.system.domain.MenuDO;
import io.github.tesla.ops.system.domain.Tree;

@Service
public interface MenuService {
  Tree<MenuDO> getSysMenuTree(Long id);

  List<Tree<MenuDO>> listMenuTree(Long id);

  Tree<MenuDO> getTree();

  Tree<MenuDO> getTree(Long id);

  List<MenuDO> list();

  int remove(Long id);

  int save(MenuDO menu);

  int update(MenuDO menu);

  MenuDO get(Long id);

}
