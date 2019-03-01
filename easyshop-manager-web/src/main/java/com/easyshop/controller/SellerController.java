package com.easyshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.Seller;
import com.easyshop.service.SellerService;
import com.easyshop.utils.PageResult;
import com.easyshop.utils.ResultMsg;

@Controller
@RequestMapping("/seller")
public class SellerController {
	
	@Reference
	SellerService sellerService;
	
	
	// 查询首页
		@RequestMapping("/list/{pageIndex}")
		public String list(Seller seller, @PathVariable("pageIndex") Integer pageIndex,
				@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize, Model model) {
			
			Page<Seller> results = null;
			// 分页工具
			Page<Seller> page = new Page<Seller>(pageIndex, pageSize);
			EntityWrapper<Seller> entityWrapper = new EntityWrapper<Seller>();
			if (seller != null && seller.getName() != null) {
				entityWrapper.like("name", seller.getName()); //公司名查询
			}
			// 店铺名字  查询条件2个
			if (seller != null && seller.getNickName() != null) {
				entityWrapper.like("nick_name", seller.getNickName());  //店铺名字查询
			} 
			entityWrapper.eq("status", 0);  //查询待审核的店铺查询   status=0
			entityWrapper.eq("del", 0);  //未删除的商家
			
			results = sellerService.selectPage(page, entityWrapper);
			// 获取总数
			int totalCount = ((Long) results.getTotal()).intValue();
			// 查询是否有上一页
			boolean hasPrevious = results.hasPrevious();
			// 查询是否有下一页
			boolean hasNext = results.hasNext();
			// 查询到每页数据
			List<Seller> sellers = results.getRecords();
			// 查询出所有班级
			PageResult<Seller> pageResult = new PageResult<Seller>(totalCount, pageIndex, pageSize, sellers, seller);
			model.addAttribute("pageResult", pageResult);
			model.addAttribute("hasPrevious", hasPrevious);
			model.addAttribute("hasNext", hasNext);
			return "seller"; // 跳转模板上
		}
		
		
		/**
		 * 审核
		 * @return
		 */
		@RequestMapping("/updateStatus")
		@ResponseBody
		public ResultMsg updateStatus(Integer id,String status){
			Seller seller = sellerService.selectById(id);
			seller.setStatus(status);
			
			boolean b;
			ResultMsg result = null;
			try {
				b = sellerService.updateById(seller);
				if (b == true) {
					result = new ResultMsg(true, "操作成功");
				} else {
					result = new ResultMsg(false, "操作失败");
				}
			} catch (Exception e) {
				result = new ResultMsg(false, "操作失败");
			}
			return result;
		}
		

}
