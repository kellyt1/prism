package us.mn.state.health.model.common;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class RequestEvaluation implements Serializable {
    private Long requestEvaluationId;
    private Group evaluatorGroup;                               //the set of people who MAY end up being the evaluator
    private Person evaluator;                                   //the user (member of evaluatorRole) who evaluated
    private Status evaluationDecision;
    private Date evaluationDate;
    private int version;
    private DAOFactory daoFactory = new HibernateDAOFactory();
    private Integer approvalLevel;
    private Status firstStatus;

    public Status getFirstStatus() {
        return firstStatus;
    }

    public void setFirstStatus(Status firstStatus) {
        this.firstStatus = firstStatus;
    }

    public void save() throws InfrastructureException {
        daoFactory.getRequestEvaluationDAO().makePersistent(this);
    }

    public void delete() throws InfrastructureException {
        daoFactory.getRequestEvaluationDAO().makeTransient(this);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Request Evaluation ID: " + this.requestEvaluationId + "\n");
        buffer.append("evaluatorGroup (code): " + (this.evaluatorGroup == null?"":this.evaluatorGroup.getGroupCode()) + "\n");
        buffer.append("evaluationDate: " + (this.evaluationDate == null? "":this.evaluationDate) + "\n");
        buffer.append("evaluationDecision: " + (this.evaluationDecision==null?"":this.evaluationDecision.getName()) + "\n");
        if (evaluator != null) {
            buffer.append("evaluator: " + this.evaluator.getFirstAndLastName());
        }
        buffer.append("approvalLevel: " + this.approvalLevel + "\n");
        buffer.append("firstStatus: " + (this.firstStatus==null?"":this.firstStatus.getName()) + "\n");
        return buffer.toString();
    }

    public boolean isWaitingForApproval() {
        return isStatus(Status.WAITING_FOR_APPROVAL);
    }

    private boolean isStatus(String statusCode) {
        if (evaluationDecision == null) {
            throw new IllegalStateException("evaluationDecision is null");
        }
        return evaluationDecision.getStatusCode().equals(statusCode);
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Status getEvaluationDecision() {
        return evaluationDecision;
    }

    public void setEvaluationDecision(Status evaluationDecision) {
        this.evaluationDecision = evaluationDecision;
    }

    public Person getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Person evaluator) {
        this.evaluator = evaluator;
    }

    public Group getEvaluatorGroup() {
        return evaluatorGroup;
    }

    public void setEvaluatorGroup(Group evaluatorGroup) {
        this.evaluatorGroup = evaluatorGroup;
    }

    public Long getRequestEvaluationId() {
        return requestEvaluationId;
    }

    public void setRequestEvaluationId(Long requestEvaluationId) {
        this.requestEvaluationId = requestEvaluationId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(Integer approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    //used in showEvaluationStatus.jsp
    public ArrayList<Person> getGroupMembers() {
        return new ArrayList<Person>(evaluatorGroup.getPersonGroupLinks());
    }
}