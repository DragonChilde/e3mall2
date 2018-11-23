package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private TbUserMapper userMapper;
	


	@Override
	public E3Result checkData(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if(type ==1 )
		{
			criteria.andUsernameEqualTo(param);
		}else if(type ==2)
		{
			criteria.andPhoneEqualTo(param);
		}else if(type == 3)
		{
			criteria.andEmailEqualTo(param);
		}else {
			return E3Result.build(400, "error param!");
		}
		List<TbUser> user = userMapper.selectByExample(example);
		if(user.size() ==0 || user == null)
		{
			return  E3Result.ok(true);
		}
		return E3Result.ok(false);
	}

	@Override
	public E3Result register(TbUser user) {
		if(StringUtils.isBlank(user.getUsername()))
		{
			return E3Result.build(400, "username is empty!");
		}
		if(StringUtils.isBlank(user.getPassword()))
		{
			return E3Result.build(400, "password is empty!");
		}
		
		E3Result result = checkData(user.getUsername(), 1);
		if(!(boolean)result.getData())
		{
			return E3Result.build(400, "username is already use!");
		}
		
		if(StringUtils.isNotBlank(user.getPhone()))
		{
			E3Result e3Result = checkData(user.getPhone(),2);
			if(!(boolean)e3Result.getData())
			{
				return E3Result.build(400, "phone is already use!");
			}
		}
		
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String md5Digest = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Digest);
		userMapper.insert(user);
		return E3Result.ok();
	}


}
