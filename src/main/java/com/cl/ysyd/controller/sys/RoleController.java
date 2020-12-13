package com.cl.ysyd.controller.sys;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.BindingRoleAndMenuReqDto;
import com.cl.ysyd.dto.sys.req.TsRoleReqDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.cl.ysyd.service.sys.IRoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 角色控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="角色接口")
@RequestMapping(value = "/v1/role")
@CrossOrigin
public class RoleController {
    /**
     * 角色service
     */
    @Autowired
    private IRoleService iRoleService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping(value = "/{pkId}")
    @LoggerManage(description = "删除角色")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iRoleService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TsRoleResDto>
     */
    @ApiOperation(value = "根据主键查询角色")
    @GetMapping(value = "/{pkId}")
    @LoggerManage(description = "根据主键查询角色")
    public ResponseData<TsRoleResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TsRoleResDto resDto =  this.iRoleService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增角色")
    @PostMapping(value = "")
    @LoggerManage(description = "新增角色")
    public ResponseData<Integer> createRole(@RequestBody @Valid TsRoleReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iRoleService.createRole(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改角色/禁用/启用")
    @PutMapping(value = "/{pkId}")
    @LoggerManage(description = "修改角色/禁用/启用")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TsRoleReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iRoleService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    @ApiOperation(value = "查询角色列表")
    @GetMapping(path = "/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询角色列表")
    public ResponseData<PageInfo<TsRoleResDto>> queryRoleByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String roleName, String isAll, String status) {
        log.info("Controller queryRoleByPage start.");
        PageInfo<TsRoleResDto> retDto = this.iRoleService.queryRoleByPage(pageNum, pageSize, roleName, isAll, status);
        log.info("Controller queryRoleByPage end.");
        return new ResponseData<>(retDto);
    }

    /**
     * 用户绑定角色
     * @param reqDto 请求对象
     * @return 返回结果
     */
    @ApiOperation(value = "角色绑定菜单和按钮")
    @PutMapping(path = "/binding")
    @LoggerManage(description = "角色绑定菜单和按钮")
    public ResponseData<String> bindingRole( @RequestBody @Valid BindingRoleAndMenuReqDto reqDto){
        log.info("Controller bindingRole start.");
        this.iRoleService.bindingMenu(reqDto);
        log.info("Controller bindingRole end. ");
        return new ResponseData<>("分配菜单按钮成功!");
    }

    /**
     * 查询所有角色
     * @return 所有角色结果集
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping(path = "/queryAll")
    @LoggerManage(description = "查询所有角色")
    public ResponseData<Map<String, String>> queryAll(){
        log.info("Controller queryAll start.");
        Map<String, String> roleResDtoList = this.iRoleService.queryAll();
        log.info("Controller queryAll end. ");
        return new ResponseData<>(roleResDtoList);
    }
}
