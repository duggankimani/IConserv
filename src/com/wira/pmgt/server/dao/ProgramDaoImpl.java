package com.wira.pmgt.server.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.dao.biz.model.Fund;
import com.wira.pmgt.server.dao.biz.model.Period;
import com.wira.pmgt.server.dao.biz.model.ProgramDetail;
import com.wira.pmgt.server.dao.biz.model.ProgramFund;
import com.wira.pmgt.server.dao.biz.model.TargetAndOutcome;
import com.wira.pmgt.server.dao.model.PO;
import com.wira.pmgt.server.helper.auth.LoginHelper;
import com.wira.pmgt.server.helper.session.SessionHelper;
import com.wira.pmgt.shared.model.HTUser;
import com.wira.pmgt.shared.model.PermissionType;
import com.wira.pmgt.shared.model.ProgramDetailType;
import com.wira.pmgt.shared.model.UserGroup;
import com.wira.pmgt.shared.model.program.Metric;
import com.wira.pmgt.shared.model.program.PerformanceModel;
import com.wira.pmgt.shared.model.program.ProgramAnalysis;
import com.wira.pmgt.shared.model.program.ProgramStatus;
import com.wira.pmgt.shared.model.program.ProgramSummary;
import com.wira.pmgt.shared.model.program.ProgramTaskForm;
import com.wira.pmgt.shared.model.program.ProgramTreeModel;

/**
 * Dao Implementation Class for managing
 * Fund, ProgramFund, Program, ProgramDetail 
 * <p><p>
 * @author duggan
 *
 */
public class ProgramDaoImpl extends BaseDaoImpl{

	private Logger log = Logger.getLogger(ProgramDaoImpl.class);
	
	public ProgramDaoImpl(EntityManager em) {
		super(em);
	}
	
	/**
	 * ProgramDetail status change from CREATED to Assigned should 
	 * commit the budget amount; ideally indicating the amount budgeted
	 * as already used(before the end user comes in to indicate what the actual expenditure is)
	 * 
	 * For this reason, we override save to update all program funds
	 * <p>
	 * 
	 * Saving Source of Funds {@link ProgramFund} updates ProgramDetail 
	 * bugetAmount, actualAmount, commitedAmount through a trigger procedure
	 * proc_saveallocations(). <br>
	 * This leads to an inconsistency between the uncommited DB row & the hibernate's 
	 * cached ProgramDetail entity, we need to synchronize the updates 
	 * made to the ProgramDetail record by the trigger with the ProgramDetail Entity.
	 * We do this by flushing the EntityManager & Refreshing the PO
	 * <p>
	 * 
	 * <a href="http://stackoverflow.com/questions/6980875/database-trigger-and-hibernate">database-trigger-and-hibernate</a>
	 */
	public void save(PO po) {
//		if(po instanceof ProgramDetail && po.getId()!=null){
//			//This only happens in an update
//			ProgramDetail detail = (ProgramDetail)po;
//			
//			if(detail.getType()!=ProgramDetailType.PROGRAM)
//			if(detail.getStatus()!=ProgramStatus.CREATED && detail.getStatus()!=ProgramStatus.COMPLETED){
//				Collection<ProgramFund> funds = detail.getSourceOfFunds();
//				for(ProgramFund fund: funds){
//					if(fund.getCommitedAmount()!=fund.getAmount()){//Amount budget vs amount
//						fund.commitFunds();
//					}
//				}
//				//detail.setSourceOfFunds(funds);
//			}
//			
//		}
		super.save(po);
		
//		try{
//			assert DB.hasActiveTrx();
//		}catch(SystemException | NamingException e){e.printStackTrace();}		
		
		//em.flush();
		//em.refresh(po);
		
	}
	
	public Period getActivePeriod(){
		Query query = em.createNamedQuery("Period.findActive")
				.setParameter("isActive", 1)
				.setParameter("now", new Date());
		
		List<Period> periods = getResultList(query);
		
		if(periods!=null && !periods.isEmpty()){
			if(periods.size()==1){
				return periods.get(0);
			}				
			if(periods.size()>1){
				Period period = periods.get(0);
				log.error("Period.findActive returns more than 1 active periods: using first result ["+period+"]");
				return period;
			}
		}
		
		return null;
	}
	
	public List<Period> getPeriods(){
		Query query = em.createNamedQuery("Period.findAll");
		return getResultList(query);
	}
	
	public List<Fund> getFunds(){
		Query query = em.createNamedQuery("Fund.findAll").setParameter("isActive", 1);
		return getResultList(query);
	}
	
	public List<ProgramDetail> getProgramDetails(ProgramDetailType type, Period period){
		return getProgramDetails(type, period, getCurrentUserId(), getCurrentUserGroups());
	}
	
	private String getCurrentUserId() {
		HTUser user = SessionHelper.getCurrentUser();
		assert user!=null;
		
		return user.getUserId();
	}

	private List<String> getCurrentUserGroups() {
		String userId = getCurrentUserId();
		return getCurrentUserGroups(userId);
	}
	
	private List<String> getCurrentUserGroups(String userId) {
		
		List<UserGroup> groups = LoginHelper.get().getGroupsForUser(userId);
		if(groups==null){
			log.warn("Get Program Details- No Results Due To: User '"+userId+"'has no groups");
			return new ArrayList<>();
		}
		
		List<String> groupIds = new ArrayList<>();
		for(UserGroup group: groups){
			groupIds.add(group.getEntityId());
		}
		
		return groupIds;
	}
	
	public List<ProgramDetail> getProgramDetails(ProgramDetailType type, Period period,String user, List<String> groups){
		if(type==null){
			return getProgramDetails(period);
		}
		
		Query query = null;
		
		if(type.equals(ProgramDetailType.OBJECTIVE) || type.equals(ProgramDetailType.OUTCOME)){
			query = em.createNamedQuery("ProgramDetail.findByType")
			.setParameter("isCurrentUserAdmin", groups.contains("ADMIN") || type==ProgramDetailType.OUTCOME)
			.setParameter("userId", user)
			.setParameter("groupIds", groups)
			.setParameter("type", type)
			.setParameter("isActive", 1);
		}else{
			if(period==null){
				return new ArrayList<>();
			}
			query=em.createNamedQuery("ProgramDetail.findByTypeAndPeriod")
					.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
					.setParameter("userId", user)
					.setParameter("groupIds", groups)
					.setParameter("type", type)
					.setParameter("period", period)
					.setParameter("isActive", 1);
		}
		
		
		return getResultList(query);
	}

	public List<ProgramDetail> getProgramDetails(Period period) {		
		return getProgramDetails(period,getCurrentUserId() , getCurrentUserGroups());
	}
	
	public List<ProgramDetail> getProgramActivitiesByOutcome(Long programId,
			Long outcomeId) {
		
		Query query = em.createNamedQuery("ProgramDetail.findActivitiesByProgramAndOutcome")
				.setParameter("parentId", programId)
				.setParameter("activityOutcomeId", outcomeId)
				.setParameter("isActive", 1);
				
		return getResultList(query);
				
	}
	public List<ProgramDetail> getProgramDetails(Period period, String user, List<String> groups) {
		if(groups==null || groups.isEmpty()){
			log.warn("Get Program Details- No Results Due To: User '"+user+"'has no groups");
			return new ArrayList<>();
		}
		
		if(period==null){
			return new ArrayList<>();
		}
		
		Query query = em.createNamedQuery("ProgramDetail.findAll")
		.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
		.setParameter("userId", user)
		.setParameter("groupIds", groups)
		.setParameter("isActive", 1)
		.setParameter("period", period);
		
		return getResultList(query);
	}

	public Period getPeriod(Long id) {
		Query query = em.createNamedQuery("Period.findById").setParameter("id", id);
		return getSingleResultOrNull(query);
	}

	public ProgramDetail getProgramDetail(Long id) {
		Query query = em.createNamedQuery("ProgramDetail.findById").setParameter("id", id);
		return getSingleResultOrNull(query);
	}

	public long getSourceOfFundId(Long programFundId) {
		Query query = em.createNativeQuery("select fundid from ProgramFund where id=?")
				.setParameter(1, programFundId);
		return ((Number) query.getSingleResult()).longValue();
	}

	public ProgramDetail getByCodeAndPeriod(String code, Long periodId) {

		Period period = null;
		
		if(periodId==null){
			 period= getActivePeriod();
		}else{
			period = getPeriod(periodId);
		}
		
		if(period==null)
			return null;
		
		Query query = em.createNamedQuery("ProgramDetail.findByCodeAndPeriod")
				.setParameter("code", code)
				.setParameter("period", period);
		
		return getSingleResultOrNull(query);
	}
	
	public List<ProgramSummary> getProgramCalendar(String userId){
		return getProgramCalendar(userId, 7);
	}
	
	public List<ProgramSummary> getProgramCalendar(String userId, int days){
		List<Long> ids = getProgramIds(userId);
		if(ids.isEmpty()){
			log.warn("No ids found......... Cannot load calendar for current user");
			return new ArrayList<>();
		}
		
		log.debug("Ids found >> "+ids);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		
		Date upcomingDate = calendar.getTime();
		System.err.println(upcomingDate);
		Query query = em.createNamedQuery("ProgramDetail.getCalendar")
				.setParameter("parentIds", ids)
				.setParameter("statusCreated", ProgramStatus.CREATED.name())
				.setParameter("currentDate", new Date())
				.setParameter("statusClosed", ProgramStatus.CLOSED.name())
				.setParameter("upcomingDate", upcomingDate);	
		
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramSummary> summaries = new ArrayList<>();
		for(Object[] row: rows){
			//programId,id,parentid,type,startdate,enddate,status
			int i=0;
			Object value=null;
			Long programId= (value=row[i++])==null? null: new Long(value.toString());
			Long id=(value=row[i++])==null? null: new Long(value.toString());
			Long parentid=(value=row[i++])==null? null: new Long(value.toString());
			String type=(value=row[i++])==null? null: value.toString();
			Date startdate=(value=row[i++])==null? null: (Date)value;
			Date enddate=(value=row[i++])==null? null: (Date)value;
			String status=(value=row[i++])==null? ProgramStatus.CREATED.name(): value.toString();
			String name = (value=row[i++])==null? null: value.toString();
			String description=(value=row[i++])==null? null: value.toString();
			
			ProgramSummary summary = new ProgramSummary(name,description,
					programId,id,parentid,ProgramDetailType.valueOf(type),startdate,enddate,
					ProgramStatus.valueOf(status));
			
			summaries.add(summary);
		}
		return summaries;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getProgramIds(String userId) {
		List<String> groups = getCurrentUserGroups(userId);
		if(groups==null || groups.isEmpty()){
			return new ArrayList<>();
		}
		
		Period period = getActivePeriod();
		
		if(period==null){
			return new ArrayList<>();
		}
		
		Query query = em.createNamedQuery("ProgramDetail.findAllIds")
		.setParameter("isCurrentUserAdmin", groups.contains("ADMIN"))
		.setParameter("type", ProgramDetailType.PROGRAM)
		.setParameter("userId", userId)
		.setParameter("groupIds", groups)
		.setParameter("isActive", 1)
		.setParameter("period", period);
				
		return getResultList(query);
	}

	public List<ProgramTaskForm> getTaskFormsForProgram(Long programId) {
		List<String> formsNames = Arrays.asList("Program-"+programId,"Program-"+programId+"-Approval" );
		Query query = em.createNamedQuery("ProgramDetail.getTaskForms")
				.setParameter("formNames", formsNames);	
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramTaskForm> forms = new ArrayList<>();
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			Long formId= (value=row[i++])==null? null: new Long(value.toString());
			String caption = (value=row[i++])==null? null: value.toString();
			
			ProgramTaskForm form = new ProgramTaskForm(formId,caption);
			
			forms.add(form);
		}
		
		return forms;
	}

	public List<ProgramAnalysis> getAnalysisData(Long periodId) {
		if(periodId==null){
			return new ArrayList<>();
		}
		Query query = em.createNamedQuery("ProgramDetail.getAnalysisData")
				.setParameter("periodid", periodId);	
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramAnalysis> list = new ArrayList<>();
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			Long id= (value=row[i++])==null? null: new Long(value.toString());
			String name = (value=row[i++])==null? null: value.toString();
			String description=(value=row[i++])==null? null: value.toString();
			Double budgetAmount=(value=row[i++])==null? null: new Double(value.toString()); //Total budget amount (accumulation of source of funds)
			Double actualAmount=(value=row[i++])==null? null: new Double(value.toString()); //Actual amount spent
			Double commitedAmount=(value=row[i++])==null? null: new Double(value.toString());
			Double totalAllocation=(value=row[i++])==null? null: new Double(value.toString());
			
			ProgramAnalysis analysis = new ProgramAnalysis(id, name, description, budgetAmount, actualAmount, commitedAmount, totalAllocation);
			list.add(analysis);
		}
		return list;
	}

	public Double[] getAmounts(Long programId, Long fundid) {
		String countQuery = "select count(*) from programdetail where parentid=:parentId";
		Query query = em.createNativeQuery(countQuery).setParameter("parentId", programId);
		Integer count = ((Number)getSingleResultOrNull(query)).intValue(); 
		
		//Commited amount, Actual amount
		Double[] values = new Double[2];
		
		if(count==0){
			values[0]=0.0;
			values[1]=0.0;
			return values;
		}
		
		String aggregateQuery= "select sum(commitedamount) commited,sum(actualamount) actual "
				+ "from programfund "
				+ "where programid in(select id from programdetail where parentid=:parentid) "
				+ "and fundid=:fundid";
		
		query = em.createNativeQuery(aggregateQuery)
				.setParameter("parentid", programId)
				.setParameter("fundid", fundid);
		
		List<Object[]> rows = getResultList(query);
		if(!rows.isEmpty()){
			Object[] row = rows.get(0);
			values[0] = (Double)row[0];
			values[1] = (Double)row[1];
		}
		return values;
	}
	
	public List<PerformanceModel> getBudgetPerformanceData(Metric metric, Long periodId){
		if(periodId==null){
			return new ArrayList<>();
		}
		List<PerformanceModel> list = new ArrayList<>();
		String analysis = null;
		switch (metric) {
		case BUDGET:
			analysis = "ProgramDetail.getBudgetAnalysis";
			break;
		case TARGETS:
			analysis = "ProgramDetail.getPerformanceByKPIs";
			break;
		
		case THROUGHPUT:
			
			break;
			
		case TIMELINES:
			analysis = "ProgramDetail.getPerfomanceByTimelines";
			break;
		}
		
		if(analysis==null){
			return list;
		}
		
		Query query = em.createNamedQuery(analysis).setParameter("periodid", periodId.intValue());	
		List<Object[]> rows = getResultList(query); 
		
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			String name = (value=row[i++])==null? null: value.toString();
			String description=(value=row[i++])==null? null: value.toString();
			Long id= (value=row[i++])==null? null: new Long(value.toString());
			int countSuccess=(value=row[i++])==null? null: new Integer(value.toString());
			int countFail=(value=row[i++])==null? null: new Integer(value.toString());
			int countNoData=(value=row[i++])==null? null: new Integer(value.toString());
			double percSuccess=(value=row[i++])==null? null: new Double(value.toString());
			double percFail=(value=row[i++])==null? null: new Double(value.toString());
			double avgPerSuccess=(value=row[i++])==null? null: new Double(value.toString());
			
			
			PerformanceModel perf = new PerformanceModel();
			perf.setProgramId(id);
			perf.setName(name);
			perf.setDescription(description);
			perf.setCountSuccess(countSuccess);
			perf.setCountFail(countFail);
			perf.setCountNoData(countNoData);
			perf.setPercSuccess(percSuccess);
			perf.setPercFail(percFail);
			perf.setAvgPerSuccess(avgPerSuccess);
			
			list.add(perf);
		}
		return list;
	}
	
	public Double[] getComputedAmounts(Long id){
		String sql = "select budgetamount, actualamount,commitedamount from programdetail where id=:id";
		Query query = em.createNativeQuery(sql).setParameter("id", id);	
		List<Object[]> rows = getResultList(query);
		
		Double[] values = new Double[3];
		if(rows.size()==1){
			Object[] row = rows.get(0);
			Object value=null;
			int i=0;
			double budgetAmount=(value=row[i++])==null? null: new Double(value.toString());
			double actualAmount=(value=row[i++])==null? null: new Double(value.toString());
			double commitedAmount=(value=row[i++])==null? null: new Double(value.toString());
			
			values[0]=budgetAmount;
			values[1]=actualAmount;
			values[2]=commitedAmount;
		}
		
		return values;
		
	}

	public void flush() {
		em.flush();
	}

	public void refresh(ProgramDetail managedPO) {
		em.refresh(managedPO);
	}

	public void deleteProgramFunds(List<Long> idsToDelete) {
		if(idsToDelete==null || idsToDelete.isEmpty()){
			return;
		}
		
		Query query = em.createNativeQuery("delete from ProgramFund where id in (:ids)")
				.setParameter("ids", idsToDelete);
		query.executeUpdate();
	}

	public List<Long> getPreviousFundIds(Long programId) {
		Query query = em.createNativeQuery("select id from ProgramFund where programid=?")
				.setParameter(1, programId);
		List<Long> ids = new ArrayList<>();
		
		List<BigInteger> values =  getResultList(query);
		for(BigInteger programfundId: values){
			ids.add(programfundId.longValue());
		}
		return ids;
	}

	/**
	 * 
	 * @param programId
	 * @return true is program is assigned
	 */
	public boolean isProgramAssigned(Long programId) {
		String countQuery = "select count(*) from programaccess where type!='CAN_EDIT' and programdetailid=:programId";
		Query query = em.createNativeQuery(countQuery).setParameter("programId", programId);
		Integer count = ((Number)getSingleResultOrNull(query)).intValue(); 
		
		return count>0;
	}

	/**
	 * 
	 * @param periodId
	 * @param programId
	 * @return
	 */
	public List<ProgramTreeModel> getProgramTree(Long periodId,
			List<Long> programIds) {
		
		if(programIds==null || programIds.isEmpty()){
			return new ArrayList<>();
		}
		
		Query query = em.createNamedQuery("ProgramDetail.getProgramTree")
				.setParameter("programIds", programIds)
				.setParameter("periodId", periodId);	
		
		List<Object[]> rows = getResultList(query); 
		
		List<ProgramTreeModel> summaries = new ArrayList<>();
		
		for(Object[] row: rows){
			//programId,id,parentid,type,startdate,enddate,status
			int i=0;
			Object value=null;
			Long programId= (value=row[i++])==null? null: new Long(value.toString());
			Long id=(value=row[i++])==null? null: new Long(value.toString());
			Long parentid=(value=row[i++])==null? null: new Long(value.toString());
			String type=(value=row[i++])==null? null: value.toString();
			String status=(value=row[i++])==null? ProgramStatus.CREATED.name(): value.toString();
			Long outcomeId=(value=row[i++])==null? null: new Long(value.toString());
			String name = (value=row[i++])==null? null: value.toString();
			String outcomeName = (value=row[i++])==null? null: value.toString();
			
			ProgramTreeModel summary = new ProgramTreeModel(name,
					programId,id,parentid,ProgramDetailType.valueOf(type),
					ProgramStatus.valueOf(status), outcomeId,outcomeName);
			
			summaries.add(summary);
		}
		
		return summaries;
	}

	public void moveToOutcome(Long itemToMoveId, Long outcomeId) {
		String sql="update programdetail set outcomeId=? where id=?";
		Query query = em.createNativeQuery(sql).setParameter(1, outcomeId).setParameter(2, itemToMoveId);
		query.executeUpdate();
	}

	public void moveToParent(Long itemToMoveId, Long parentId) {
		String sql="update programdetail set parentId=? where id=?";
		Query query = em.createNativeQuery(sql).setParameter(1, parentId).setParameter(2, itemToMoveId);
		query.executeUpdate();
	}

	public ProgramStatus getStatus(Long programDetailId) {
		String countQuery = "select status from programdetail where id=:programId";
		Query query = em.createNativeQuery(countQuery).setParameter("programId", programDetailId);
		String value = getSingleResultOrNull(query);
		return value==null? ProgramStatus.CREATED: ProgramStatus.valueOf(value);
	}

	public double getOutcome(String key, Long programId) {
		String sql = "select sum(actualoutcome) from targetandoutcome "
				+ "where key=:key "
				+ "and programid in (select id from programdetail where parentid=:programId)";
		Query query = em.createNativeQuery(sql)
				.setParameter("programId", programId)
				.setParameter("key", key);
		Number value = getSingleResultOrNull(query);
		return value==null? 0.0: value.doubleValue();
	}

	public void deleteTargetsNotIn(ProgramDetail detail, List<Long> ids) {
		if(ids.isEmpty()){
			return;
		}
		
		Query query = em.createQuery("FROM TargetAndOutcome t where t.programDetail=:programDetail "
						+ "and t.id not in (:ids)")
						.setParameter("programDetail", detail)
						.setParameter("ids", ids);
		
		List<TargetAndOutcome> ts = getResultList(query);
		
		for(TargetAndOutcome t: ts){
			delete(t);
		}
		
	}
	
	public HashMap<Long, PermissionType> getPermissions(String userId, List<String> groupIds, Long periodId){
		String sql = "select distinct p.id pid, a.type from programdetail p "
				+ "inner join programaccess a on(a.programdetailid=p.id) "
				+ "where p.type='PROGRAM' "
				+ "and p.periodId=:periodId "
				+ "and (userId=:userId "
				+ "or a.groupid in (:groups)) "
				+ "order by pid, type desc";

		Query query = em.createNativeQuery(sql)
				.setParameter("periodId", periodId==null? -1L: periodId)
				.setParameter("userId", userId)
				.setParameter("groups", groupIds);
		List<Object[]> rows = getResultList(query); 
		
		HashMap<Long, PermissionType> permissions = new HashMap<Long, PermissionType>();
		for(Object[] row: rows){
			permissions.put(new Long(row[0].toString()), PermissionType.valueOf(row[1].toString()));
		}
		
		return permissions;
	}
}
