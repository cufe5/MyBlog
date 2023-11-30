package com.yujian.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Category;
import com.yujian.domain.vo.CategoryVo;
import com.yujian.domain.vo.ExcelCategoryVo;
import com.yujian.enums.AppHttpCodeEnum;
import com.yujian.service.CategoryService;
import com.yujian.utils.BeanCopyUtils;
import com.yujian.utils.ObjectList;
import com.yujian.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.getListAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);

            //获取需要导出的数据
            //Object obj = categoryService.getListAllCategory().getData();
            //List<CategoryVo> categoryVos = ObjectList.castList(obj, CategoryVo.class);
            List<Category> categoryList = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);

            //把数据写入excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //throw new RuntimeException(e);
            //如果出现异常要响应json数据
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));

        }
    }

    @GetMapping("/list")
    public ResponseResult queryPage(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.pageCategory(pageNum,pageSize,name,status);
    }
    @PostMapping()
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }
    @GetMapping("/{id}")
    public ResponseResult queryById(@PathVariable("id") Long id){
        return ResponseResult.okResult(categoryService.getById(id));
    }
    @PutMapping
    public ResponseResult modifyCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
