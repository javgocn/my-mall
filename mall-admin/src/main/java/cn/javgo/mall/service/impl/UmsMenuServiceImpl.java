package cn.javgo.mall.service.impl;

import cn.javgo.mall.dto.UmsMenuNode;
import cn.javgo.mall.mapper.UmsMenuMapper;
import cn.javgo.mall.model.UmsMenu;
import cn.javgo.mall.model.UmsMenuExample;
import cn.javgo.mall.service.UmsMenuService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理 Service 实现类
 */
@Service
public class UmsMenuServiceImpl implements UmsMenuService {

    @Autowired
    private UmsMenuMapper umsMenuMapper;

    @Override
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return umsMenuMapper.insert(umsMenu);
    }

    /**
     * 修改菜单层级
     * @param umsMenu 菜单
     */
    private void updateLevel(UmsMenu umsMenu){
        // 检查是否有父菜单, 没有父菜单时为一级菜单(菜单级数从 0 开始)
        if (umsMenu.getParentId() == 0){
            umsMenu.setLevel(0);
        }else {
            // 有父菜单时选择根据父菜单 level 设置
            UmsMenu parentMenu = umsMenuMapper.selectByPrimaryKey(umsMenu.getParentId());
            if (parentMenu != null){
                // 存在父菜单时, 菜单的级数为父菜单的级数 + 1
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            }else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu);
    }

    @Override
    public UmsMenu getItem(Long id) {
        return umsMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return umsMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsMenuExample example = new UmsMenuExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return umsMenuMapper.selectByExample(example);
    }

    @Override
    public List<UmsMenuNode> treeList() {
        // 查询所有菜单
        List<UmsMenu> menuList = umsMenuMapper.selectByExample(new UmsMenuExample());
        // 将 UmsMenu 转化为 UmsMenuNode 并设置 children 属性
        List<UmsMenuNode> result = menuList.stream()
                // 过滤出所有一级菜单
                .filter(menu -> menu.getParentId().equals(0L))
                // 递归将一级菜单转化为 UmsMenuNode, 并设置 children 属性
                .map(menu -> coverMenuNode(menu, menuList))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 将 UmsMenu 转化为 UmsMenuNode 并设置 children 属性
     * @param menu 菜单
     * @param menuList 菜单列表
     * @return UmsMenuNode
     */
    private UmsMenuNode coverMenuNode(UmsMenu menu,List<UmsMenu> menuList){
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(menu,node);
        List<UmsMenuNode> children = menuList.stream()
                // 过滤出所有子菜单
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                // 递归将子菜单转化为 UmsMenuNode
                .map(subMenu -> coverMenuNode(subMenu, menuList)).collect(Collectors.toList());
        // 将子菜单设置到父菜单的 children 属性中
        node.setChildren(children);
        return node;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu);
    }
}
