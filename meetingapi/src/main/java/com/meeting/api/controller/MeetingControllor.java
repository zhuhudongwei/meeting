package com.meeting.api.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.api.bean.MeetingListBean;
import com.meeting.api.bean.MeetingMemberBean;
import com.meeting.api.bean.MemeberFileBean;
import com.meeting.api.bean.PersonBean;
import com.meeting.api.bean.StautsBean;
import com.meeting.api.model.Department;
import com.meeting.api.model.File;
import com.meeting.api.model.MeetingFile;
import com.meeting.api.model.MeetingInfo;
import com.meeting.api.model.MeetingMember;
import com.meeting.api.model.Person;
import com.meeting.api.model.Post;
import com.meeting.api.service.DepartmentRepository;
import com.meeting.api.service.FileRepository;
import com.meeting.api.service.MeetingFileRepository;
import com.meeting.api.service.MeetingInfoRepository;
import com.meeting.api.service.MeetingMemberRepository;
import com.meeting.api.service.PersonRepository;
import com.meeting.api.service.PostRepository;
import com.meeting.api.utils.DateUtil;

/**
 * 访问控制器--会议管理
 * @author zdw
 *
 */
@RestController
@RequestMapping("/meeting")
public class MeetingControllor {

	@Autowired
	private MeetingInfoRepository meetingInfoRepositoryImpl;
	@Autowired
	private MeetingMemberRepository meetingMemberRepositoryImpl;
	@Autowired
	private MeetingFileRepository meetingFileRepositoryImpl;
	@Autowired
	private FileRepository fileRepositoryImpl;
	@Autowired
	private PersonRepository personRepositoryImpl;
	@Autowired
	private DepartmentRepository departmentRepositoryImpl;
	@Autowired
	private PostRepository postRepositoryImpl;
	
	protected final String imgurlPath = "http://58.215.18.191:8080/site";
//	protected final String imgurlPath = "http://120.27.44.64:7171/site";
	
	/***
	 * 获取fileid
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/getFileKey",method = RequestMethod.GET)
	public void getFileKey(HttpServletResponse response)throws Exception{
		String id_str = null;
		try {
			Long id = fileRepositoryImpl.findMaxId()+1;
			id_str = id + "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		response.getWriter().append(id_str).flush();
	}
	
	private void updateMeetingStauts()throws Exception{
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		final Short stauts = MeetingInfo.MEETING_STATUS_NEW;
		mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
			@Override
			public Predicate toPredicate(Root<MeetingInfo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> orPredicates = new ArrayList<Predicate>();
				if(stauts != null){
					Path<Short> statusPath = root.get("meeting_status");  
					Predicate p1 = cb.equal(statusPath, stauts);
					orPredicates.add(cb.and(p1));
				}
				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p);
				return null;
			}
		});
		long nowTime = System.currentTimeMillis();
		for (int i = 0; i < mis.size(); i++) {
			if(nowTime >= mis.get(i).getMeeting_timestamp()){
				meetingInfoRepositoryImpl.updateMeetingByStauts(mis.get(i).getMeeting_id(), MeetingInfo.MEETING_STATUS_MEETING);
			}
		}
	}
	
	/***
	 * 会议档案
	 * @param user_id
	 * @param meeting_title
	 * @param meeting_status
	 * @param beginTimeStr
	 * @param endTimeStr
	 * @param pageNo
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/listMeetingInfos", method = RequestMethod.GET)
	public Map listMeetingInfos(Long user_id, String meeting_title, Short meeting_status, String orderby, String beginTimeStr, String endTimeStr, 
			Integer pageNo, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			int size = 10;
			Long beginTime = null;
			Long endTime = null;
			if(meeting_title != null){
				if(meeting_title.trim().length() == 0){
					meeting_title = null;
				}
			}
			if(meeting_status != null){
				if(meeting_status == 0){
					meeting_status = null;
				}
			}
			if(beginTimeStr != null){
				if(beginTimeStr.trim().length() != 0){
					beginTime = DateUtil.convertDateInStringToLong(beginTimeStr, "yyyy-MM-dd");
				}
			}
			if(endTimeStr != null){
				if(endTimeStr.trim().length() != 0){
					endTime = DateUtil.convertDateInStringToLong(endTimeStr, "yyyy-MM-dd");
				}
			}
			if(meeting_title != null){
				meeting_title = "%"+meeting_title+"%";
			}
			final Long userid = user_id;
			final String title = meeting_title;
			final Short stauts = meeting_status;
			final Long btime = beginTime;
			final Long etime = endTime;
			final String order = orderby;
			PageRequest pr = new PageRequest(pageNo - 1, size); 
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					if(userid != null){
						Path<Long> uidPath = root.get("creator_id");  
						Predicate p1 = cb.equal(uidPath, userid);
						orPredicates.add(cb.and(p1));
					}
					if(title != null){
						Path<String> namePath = root.get("meeting_title");  
						Predicate p1 = cb.like(namePath, title);
						orPredicates.add(cb.and(p1));
					}
					if(stauts != null){
						Path<Short> statusPath = root.get("meeting_status");  
						Predicate p1 = cb.equal(statusPath, stauts);
						orPredicates.add(cb.and(p1));
					}
					if(btime != null){
						Path<Long> timePath = root.get("meeting_timestamp");  
						Predicate p1 = cb.ge(timePath, btime);
						orPredicates.add(cb.and(p1));
					}
					if(etime != null){
						Path<Long> timePath = root.get("meeting_timestamp");  
						Predicate p1 = cb.le(timePath, etime);
						orPredicates.add(cb.and(p1));
					}
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					if(order != null){
						if(order.equals("ASC")){
							query.orderBy(cb.asc(root.get("created_timestamp")));
						}else{
							query.orderBy(cb.desc(root.get("created_timestamp")));
						}
					}else{
						query.orderBy(cb.desc(root.get("created_timestamp")));
					}
					return null;
				}
			},pr).getContent();
			List<MeetingListBean> mlbs = new ArrayList<MeetingListBean>();
			for (int i = 0; i < mis.size(); i++) {
				MeetingListBean mlb = new MeetingListBean();
				mlb.setCreated_timestamp(mis.get(i).getCreated_timestamp());
				mlb.setMeeting_address(mis.get(i).getMeeting_address());
				mlb.setMeeting_id(mis.get(i).getMeeting_id());
				mlb.setMeeting_timestamp(mis.get(i).getMeeting_timestamp());
				mlb.setMeeting_title(mis.get(i).getMeeting_title());
				mlbs.add(mlb);
			}
			ma.put("MeetingInfos", mlbs);
			
			//拼装会议状态
			String []name_array = "未召开,已召开,已取消".split(",");
	        String []code_array = "1,2,9".split(",");
			List<StautsBean> sbs = new ArrayList<StautsBean>();
			for (int i = 0; i < code_array.length; i++) {
				StautsBean sb = new StautsBean();
				sb.setCode(code_array[i]);
				sb.setName(name_array[i]);
				sbs.add(sb);
			}
			ma.put("stautsSet", sbs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/***
	 * 待处理数量
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/meetingByhandleSize", method = RequestMethod.GET)
	public Map meetingByhandleSize(Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			final Long userid = user_id;
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> statusPath = join.get("member_status");  
					Predicate p2 = cb.equal(statusPath, 0);
					orPredicates.add(cb.and(p2));
					
					Path<Short> meetingstatusPath = root.get("meeting_status");  
					Predicate p3 = cb.equal(meetingstatusPath, 1);
					orPredicates.add(cb.and(p3));
					
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					return null;
				}
			});
			
			ma.put("Size", mis.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/**
	 * 待处理会议
	 * @param pageNo
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/listMeetingByhandle", method = RequestMethod.GET)
	public Map listMeetingByhandle(Integer pageNo, Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			int size = 10;
			final Long userid = user_id;
			PageRequest pr = new PageRequest(pageNo - 1, size); 
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> statusPath = join.get("member_status");  
					Predicate p2 = cb.equal(statusPath, 0);
					orPredicates.add(cb.and(p2));
					
					Path<Short> meetingstatusPath = root.get("meeting_status");  
					Predicate p3 = cb.equal(meetingstatusPath, 1);
					orPredicates.add(cb.and(p3));
					
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					query.orderBy(cb.desc(root.get("created_timestamp")));
					return null;
				}
			},pr).getContent();
			List<MeetingListBean> mlbs = new ArrayList<MeetingListBean>();
			for (int i = 0; i < mis.size(); i++) {
				MeetingListBean mlb = new MeetingListBean();
				mlb.setCreated_timestamp(mis.get(i).getCreated_timestamp());
				mlb.setMeeting_address(mis.get(i).getMeeting_address());
				mlb.setMeeting_id(mis.get(i).getMeeting_id());
				mlb.setMeeting_timestamp(mis.get(i).getMeeting_timestamp());
				mlb.setMeeting_title(mis.get(i).getMeeting_title());
				mlbs.add(mlb);
			}
			ma.put("MeetingInfos", mlbs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/**
	 * 已处理会议
	 * @param pageNo
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/listMeetingByhandled", method = RequestMethod.GET)
	public Map listMeetingByhandled(Integer pageNo, Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			int size = 10;
			final Long userid = user_id;
			PageRequest pr = new PageRequest(pageNo - 1, size); 
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> statusPath = join.get("member_status");  
					Predicate p2 = cb.notEqual(statusPath, 0);
					orPredicates.add(cb.and(p2));
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					query.orderBy(cb.desc(join.get("processing_timestamp")));
					return null;
				}
			},pr).getContent();
			List<MeetingListBean> mlbs = new ArrayList<MeetingListBean>();
			for (int i = 0; i < mis.size(); i++) {
				MeetingListBean mlb = new MeetingListBean();
				mlb.setCreated_timestamp(mis.get(i).getCreated_timestamp());
				mlb.setMeeting_address(mis.get(i).getMeeting_address());
				mlb.setMeeting_id(mis.get(i).getMeeting_id());
				mlb.setMeeting_timestamp(mis.get(i).getMeeting_timestamp());
				mlb.setMeeting_title(mis.get(i).getMeeting_title());
				mlbs.add(mlb);
			}
			ma.put("MeetingInfos", mlbs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/***
	 * 待参加会议数量
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/meetingByJoinSize", method = RequestMethod.GET)
	public Map meetingByJoinSize(Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			final Long userid = user_id;
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> statusPath = join.get("member_status");  
					Predicate p2 = cb.equal(statusPath, 1);
					orPredicates.add(cb.and(p2));
					
					Path<Short> meetingstatusPath = root.get("meeting_status");  
					Predicate p3 = cb.equal(meetingstatusPath, 1);
					orPredicates.add(cb.and(p3));
					
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					return null;
				}
			});
			ma.put("Size", mis.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/**
	 * 待参加会议
	 * @param pageNo
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/listMeetingByJoin", method = RequestMethod.GET)
	public Map listMeetingByJoin(Integer pageNo, Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			int size = 10;
			final Long userid = user_id;
			PageRequest pr = new PageRequest(pageNo - 1, size); 
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> statusPath = join.get("member_status");  
					Predicate p2 = cb.equal(statusPath, 1);
					orPredicates.add(cb.and(p2));
					
					Path<Short> meetingstatusPath = root.get("meeting_status");  
					Predicate p3 = cb.equal(meetingstatusPath, 1);
					orPredicates.add(cb.and(p3));
					
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					query.orderBy(cb.asc(root.get("meeting_timestamp")));
					return null;
				}
			},pr).getContent();
			List<MeetingListBean> mlbs = new ArrayList<MeetingListBean>();
			for (int i = 0; i < mis.size(); i++) {
				MeetingListBean mlb = new MeetingListBean();
				mlb.setCreated_timestamp(mis.get(i).getCreated_timestamp());
				mlb.setMeeting_address(mis.get(i).getMeeting_address());
				mlb.setMeeting_id(mis.get(i).getMeeting_id());
				mlb.setMeeting_timestamp(mis.get(i).getMeeting_timestamp());
				mlb.setMeeting_title(mis.get(i).getMeeting_title());
				mlbs.add(mlb);
			}
			ma.put("MeetingInfos", mlbs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/**
	 * 已参加会议
	 * @param pageNo
	 * @param user_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/listMeetingByJoined", method = RequestMethod.GET)
	public Map listMeetingByJoined(Integer pageNo, Long user_id, HttpServletRequest request){
		Map ma = new HashMap();
		List<MeetingInfo> mis = new ArrayList<MeetingInfo>();
		try {
			updateMeetingStauts();
			int size = 10;
			final Long userid = user_id;
			PageRequest pr = new PageRequest(pageNo - 1, size); 
			mis = meetingInfoRepositoryImpl.findAll(new Specification<MeetingInfo>() {
				@Override
				public Predicate toPredicate(Root<MeetingInfo> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					Join join =root.join(root.getModel().getList("meetingMembers", MeetingMember.class), JoinType.LEFT);
					
					Path<Short> useridPath = join.get("user_id");  
					Predicate p1 = cb.equal(useridPath, userid);
					orPredicates.add(cb.and(p1));
					
					Path<Short> meetingstatusPath = root.get("meeting_status");  
					Predicate p3 = cb.equal(meetingstatusPath, 2);
					orPredicates.add(cb.and(p3));
					
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					query.orderBy(cb.desc(root.get("meeting_timestamp")));
					return null;
				}
			},pr).getContent();
			List<MeetingListBean> mlbs = new ArrayList<MeetingListBean>();
			for (int i = 0; i < mis.size(); i++) {
				MeetingListBean mlb = new MeetingListBean();
				mlb.setCreated_timestamp(mis.get(i).getCreated_timestamp());
				mlb.setMeeting_address(mis.get(i).getMeeting_address());
				mlb.setMeeting_id(mis.get(i).getMeeting_id());
				mlb.setMeeting_timestamp(mis.get(i).getMeeting_timestamp());
				mlb.setMeeting_title(mis.get(i).getMeeting_title());
				mlbs.add(mlb);
			}
			ma.put("MeetingInfos", mlbs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ma;
	}
	
	/***
	 * 查看会议
	 * @param meeting_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/viewMeetingInfo", method = RequestMethod.GET)
	public Map viewMeetingInfo(long meeting_id, HttpServletRequest request){
		Map ma = new HashMap();
		try {
			MeetingInfo mi = meetingInfoRepositoryImpl.findOne(meeting_id);
			Person person = personRepositoryImpl.findOne(mi.getCreator_id());
			mi.setMobilephone(person.getMobilephone());
			ma.put("MeetingInfo", mi);
			List<MeetingMemberBean> mmbs = new ArrayList<MeetingMemberBean>(); 
			List<MeetingMember> mms = meetingMemberRepositoryImpl.findByMeeting_id(meeting_id);
			for (int i = 0; i < mms.size(); i++) {
				MeetingMemberBean mmb = new MeetingMemberBean();
				mmb.setAssign_id(mms.get(i).getAssign_id());
				mmb.setAssign_name(mms.get(i).getAssign_name());
				mmb.setAssign_status(mms.get(i).getAssign_status());
				mmb.setEntrust_id(mms.get(i).getEntrust_id());
				mmb.setEntrust_name(mms.get(i).getEntrust_name());
				mmb.setEntruts_status(mms.get(i).getEntruts_status());
				mmb.setMeeting_id(mms.get(i).getMeeting_id());
				mmb.setMember_id(mms.get(i).getMember_id());
				mmb.setMember_name(mms.get(i).getMember_name());
				mmb.setMember_status(mms.get(i).getMember_status());
				mmb.setProcessing(mms.get(i).getProcessing());
				mmb.setProcessing_timestamp(mms.get(i).getProcessing_timestamp());
				mmb.setUser_id(mms.get(i).getUser_id());
				person = personRepositoryImpl.findOne(mms.get(i).getUser_id());
				if(person != null){
					mmb.setMobilephone(person.getMobilephone());
				}
				mmbs.add(mmb);
			}
			ma.put("MeetingMembers", mmbs);
			List<MemeberFileBean> mfbs = new ArrayList<MemeberFileBean>();
			List<MeetingFile> mfs = meetingFileRepositoryImpl.findByMeeting_id(meeting_id);
			for (int i = 0; i < mfs.size(); i++) {
				MemeberFileBean mfb = new MemeberFileBean();
				mfb.setFile_id(mfs.get(i).getFile_id());
				mfb.setFile_target_id(mfs.get(i).getFile_target_id());
				mfb.setFile_type(mfs.get(i).getFile_type());
				mfb.setMeeting_id(mfs.get(i).getMeeting_id());
				File file = fileRepositoryImpl.findOne(mfs.get(i).getFile_target_id());
				file.setId(mfs.get(i).getFile_target_id());
				mfb.setFile_url(imgurlPath+"/"+file.getFilePath());
				mfb.setName(file.getName());
				mfb.setExt(file.getExt());
				mfbs.add(mfb);
			}
			ma.put("MeetingFiles", mfbs);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ma;
	}

	/***
	 * 获取部门、级别
	 * @param Pid
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/selectDepartmentAndPost", method = RequestMethod.GET)
	public Map selectDepartmentAndPost(Long Pid, HttpServletRequest request){
		Map ma = new HashMap();
		try {
			List<Department> departments = new ArrayList<Department>();
			if(Pid == null){
				departments = departmentRepositoryImpl.selectDepartmentParent();
			}else{
				departments = departmentRepositoryImpl.findByPid(Pid);
			}
			ma.put("Departments", departments);
			List<Post> posts = postRepositoryImpl.findAll();
			ma.put("Posts", posts);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ma;
	}
	
	/***
	 * 查找人员
	 * @param departmentId
	 * @param postId
	 * @return
	 */
	@RequestMapping(value ="/selectPerson", method = RequestMethod.GET)
	public Map selectPerson(Long departmentId, Long postId){
		Map ma = new HashMap();
		try {
			final Long did = departmentId;
			final Long pid = postId;
			List<PersonBean> pbs = new ArrayList<PersonBean>();
			List<Person> persons = personRepositoryImpl.findAll(new Specification<Person>() {
				@Override
				public Predicate toPredicate(Root<Person> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					List<Predicate> orPredicates = new ArrayList<Predicate>();
					if(did != null){
						Path<Long> didPath = root.get("departmentId");  
						Predicate p1 = cb.equal(didPath, did);
						orPredicates.add(cb.and(p1));
					}
					if(pid != null){
						Path<Long> pidPath = root.get("postId");  
						Predicate p1 = cb.equal(pidPath, pid);
						orPredicates.add(cb.and(p1));
					}
					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p);
					return null;
				}
			});
			for (int i = 0; i < persons.size(); i++) {
				PersonBean pb = new PersonBean();
				pb.setComment(persons.get(i).getComment());
				pb.setDepartmentId(persons.get(i).getDepartmentId());
				Department department = departmentRepositoryImpl.findOne(pb.getDepartmentId());
				pb.setDepartmentName(department.getDepartmentName());
				pb.setEmail(persons.get(i).getEmail());
				pb.setId(persons.get(i).getId());
				pb.setInphone(persons.get(i).getInphone());
				pb.setMobilephone(persons.get(i).getMobilephone());
				pb.setOutphone(persons.get(i).getOutphone());
				pb.setPersonName(persons.get(i).getPersonName());
				pb.setPersonNo(persons.get(i).getPersonNo());
				pb.setPostId(persons.get(i).getPostId());
				Post post = postRepositoryImpl.findOne(pb.getPostId());
				pb.setPostName(post.getPostName());
				pb.setRoomNo(persons.get(i).getRoomNo());
				pb.setSeatNo(persons.get(i).getSeatNo());
				pb.setStatus(persons.get(i).getStatus());
				pbs.add(pb);
			}
			ma.put("Persons", pbs);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ma;
	}
	
	/***
	 * 上传附件
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadfile",method = RequestMethod.POST)
	public Map uploadFile(HttpServletRequest request)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
		try{
		if(null != files){
			File _f = new File();
			String fileName = files.get(0).getOriginalFilename();
			int index = fileName.lastIndexOf(".");
			if(index != -1){
				_f.setExt(fileName.substring(index+1, fileName.length()));
				_f.setName(fileName.substring(0, index));
			}else{
				_f.setExt("");
				_f.setName(fileName);
			}
			this.addFile(_f, files.get(0));
			result.put("src", _f.getFilePath());
			result.put("fid", _f.getId());
		}else{
			throw new Exception();
		}
		}catch(Exception e){
			e.printStackTrace();
			result.put("error", true);
		}
		return result;
	}
	
	private long addFile(File file, MultipartFile mfile) throws Exception {
		Long id = 0L;
		if(null != mfile){
			id = fileRepositoryImpl.findMaxId() + 1;
			file.setId(id);
			file.setStatus(File.STATUS_CREATED);
			fileRepositoryImpl.save(file);
			java.io.File parent = new java.io.File("/usr/local/images"+java.io.File.separator+file.getPath());
//			java.io.File parent = new java.io.File("D://files"+java.io.File.separator+file.getPath());
			if(!parent.exists()){
				parent.mkdirs();
			}
			java.io.File sub = new java.io.File(parent, file.getFileName());
			mfile.transferTo(sub);
		}
		return id;
	}
	
	/***
	 * 增加会议
	 * @param meeting_title
	 * @param meeting_address
	 * @param meeting_timestamp_str
	 * @param meeting_content
	 * @param personName
	 * @param personId
	 * @param fileids
	 * @param imgids
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/addMeeting", method = RequestMethod.POST)
	public Map addMeeting(long userid, String userName, String meeting_title, String meeting_address, String meeting_timestamp_str,
			String meeting_content, String userids, String usernames, String fileids, String imgids, HttpServletRequest request){
		Map ma = new HashMap();
		try {
			MeetingInfo meetingInfo = new MeetingInfo();
			meetingInfo.setMeeting_title(meeting_title);
			meetingInfo.setMeeting_address(meeting_address);
			meetingInfo.setMeeting_timestamp(DateUtil.convertDateInStringToLong(meeting_timestamp_str, "yyyy-MM-dd HH:mm"));
			meetingInfo.setCreator_id(userid);
			meetingInfo.setCreator_name(userName);
			meetingInfo.setMeeting_content(meeting_content);
			meetingInfo.setMeeting_status(MeetingInfo.MEETING_STATUS_NEW);
			meetingInfo.setCreated_timestamp(System.currentTimeMillis());
			long meeting_id = meetingInfoRepositoryImpl.findMaxId() + 1;
			meetingInfo.setMeeting_id(meeting_id);
			meetingInfoRepositoryImpl.save(meetingInfo);
			//增加参会人员
			if(userids != null){
				String []userId_array = userids.split(",");
				String []name_array = usernames.split(",");
				for (int i = 0; i < userId_array.length; i++) {
					if(userId_array[i].trim().length() != 0){
						MeetingMember mm = new MeetingMember();
						mm.setUser_id(Long.parseLong(userId_array[i].trim()));
						mm.setMember_name(name_array[i].trim());
						mm.setMember_status(MeetingMember.MEMBER_STATUS_NEW);
						mm.setMeeting_id(meeting_id);
						mm.setAssign_status(MeetingMember.MEMBER_STATUS_ASSIGN_NO);
						mm.setEntruts_status(MeetingMember.MEMBER_STATUS_ENTRUST_NO);
						long member_id = meetingMemberRepositoryImpl.findMaxId() + 1;
						mm.setMember_id(member_id);
						meetingMemberRepositoryImpl.save(mm);
					}
				}
			}
			//增加附件
			if(fileids != null){
				String []fid_array = fileids.split(",");
				for (int i = 0; i < fid_array.length; i++) {
					if(fid_array[i].trim().length() != 0){
						MeetingFile mf = new MeetingFile();
						mf.setMeeting_id(meeting_id);
						mf.setFile_type(MeetingFile.MEETINGFILE_TYPE_FILE);
						mf.setFile_target_id(Long.parseLong(fid_array[i].trim()));
						long file_id = meetingFileRepositoryImpl.findMaxId() + 1;
						mf.setFile_id(file_id);
						meetingFileRepositoryImpl.save(mf);
					}
				}
			}
			if(imgids != null){
				String []imgid_array = imgids.split(",");
				for (int i = 0; i < imgid_array.length; i++) {
					if(imgid_array[i].trim().length() != 0){
						MeetingFile mf = new MeetingFile();
						mf.setMeeting_id(meeting_id);
						mf.setFile_type(MeetingFile.MEETINGFILE_TYPE_IMG);
						mf.setFile_target_id(Long.parseLong(imgid_array[i].trim()));
						long file_id = meetingFileRepositoryImpl.findMaxId() + 1;
						mf.setFile_id(file_id);
						meetingFileRepositoryImpl.save(mf);
					}
				}
			}
			ma.put("state", 1);
		} catch (Exception e) {
			// TODO: handle exception
			ma.put("state", -1);
		}
		return ma;
	}
	
	
	/***
	 * 处理会议
	 * @param httpSession
	 * @param meetingInfo
	 * @param userids
	 * @param usernames
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/processingMeeting", method = RequestMethod.POST)
	public Map processingMeeting(long meeting_id, long userId, String userName, String userids_zp, String usernames_zp, String userids_wt, String usernames_wt, short member_status_br, String processing)throws Exception{
		Map ma = new HashMap();
		try {
			//本人参加
			System.out.println(userId);
			if(member_status_br != 0){
				System.out.println("in");
				MeetingMember mm = meetingMemberRepositoryImpl.findByMeeting_idAndUser_id(meeting_id, userId);
				mm.setMember_status(MeetingMember.MEMBER_STATUS_IN);
				mm.setProcessing(processing);
				mm.setProcessing_timestamp(System.currentTimeMillis());
				meetingMemberRepositoryImpl.save(mm);
			}else{
				//未参加，委托或指派他人处理
				if(userids_zp != null || userids_wt != null){
					System.out.println("pro");
					MeetingMember mm = meetingMemberRepositoryImpl.findByMeeting_idAndUser_id(meeting_id, userId);
					mm.setMember_status(MeetingMember.MEMBER_STATUS_PRO);
					mm.setProcessing(processing);
					mm.setProcessing_timestamp(System.currentTimeMillis());
					meetingMemberRepositoryImpl.save(mm);
				}
			}
			
			//增加指派人员
			if(userids_zp != null){
				String []userId_array = userids_zp.split(",");
				String []name_array = usernames_zp.split(",");
				for (int i = 0; i < userId_array.length; i++) {
					if(userId_array[i].trim().length() != 0){
						MeetingMember oldmm = meetingMemberRepositoryImpl.findByMeeting_idAndUser_id(meeting_id, Long.parseLong(userId_array[i].trim()));
						if(oldmm == null){
							MeetingMember mm = new MeetingMember();
							mm.setUser_id(Long.parseLong(userId_array[i].trim()));
							mm.setMember_name(name_array[i].trim());
							mm.setMember_status(MeetingMember.MEMBER_STATUS_IN);
							mm.setMeeting_id(meeting_id);
							mm.setAssign_status(MeetingMember.MEMBER_STATUS_ASSIGN_YES);
							mm.setAssign_id(userId);
							mm.setAssign_name(userName);
							mm.setEntruts_status(MeetingMember.MEMBER_STATUS_ENTRUST_NO);
							long member_id = meetingMemberRepositoryImpl.findMaxId() + 1;
							mm.setMember_id(member_id);
							meetingMemberRepositoryImpl.save(mm);
						}
					}
				}
			}
			//增加委托人员
			if(userids_wt != null){
				String []userId_array = userids_wt.split(",");
				String []name_array = usernames_wt.split(",");
				for (int i = 0; i < userId_array.length; i++) {
					if(userId_array[i].trim().length() != 0){
						MeetingMember oldmm = meetingMemberRepositoryImpl.findByMeeting_idAndUser_id(meeting_id, Long.parseLong(userId_array[i].trim()));
						if(oldmm == null){
							MeetingMember mm = new MeetingMember();
							mm.setUser_id(Long.parseLong(userId_array[i].trim()));
							mm.setMember_name(name_array[i].trim());
							mm.setMember_status(MeetingMember.MEMBER_STATUS_NEW);
							mm.setMeeting_id(meeting_id);
							mm.setAssign_status(MeetingMember.MEMBER_STATUS_ASSIGN_NO);
							mm.setEntruts_status(MeetingMember.MEMBER_STATUS_ENTRUST_YES);
							mm.setEntrust_id(userId);
							mm.setEntrust_name(userName);
							long member_id = meetingMemberRepositoryImpl.findMaxId() + 1;
							mm.setMember_id(member_id);
							meetingMemberRepositoryImpl.save(mm);
						}
					}
				}
			}
			ma.put("state", 1);
		} catch (Exception e) {
			// TODO: handle exception
			ma.put("state", -1);
		}
		return ma;
	}
	
	/***
	 * 下载图片
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value ="/viewFile", method = RequestMethod.GET)
	public Map viewFile(long id, HttpServletResponse response) throws IOException{
		Map ma = new HashMap();
		MemeberFileBean mfb = new MemeberFileBean();
		File file = fileRepositoryImpl.findOne(id);
		file.setId(id);
		mfb.setFile_url(imgurlPath+"/"+file.getFilePath());
		mfb.setName(file.getName());
		mfb.setExt(file.getExt());
		ma.put("fileBean", ma);
		return ma;
//		java.io.File f = new java.io.File("/usr/local/images" + java.io.File.separator + file.getFilePath());
//        if (!f.exists()) {
//            response.sendError(404, "File not found!");
//            return;
//        }
//        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
//        byte[] buf = new byte[1024];
//        int len = 0;
//        response.reset(); // 非常重要
//        response.setContentType("application/x-msdownload");
//        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
//        OutputStream out = response.getOutputStream();
//        while ((len = br.read(buf)) > 0){
//        	out.write(buf, 0, len);
//        }
//        br.close();
//        out.close();
	}
}
