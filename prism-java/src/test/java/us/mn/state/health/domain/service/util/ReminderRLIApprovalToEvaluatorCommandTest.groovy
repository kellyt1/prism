package us.mn.state.health.domain.service.util

import junit.framework.TestCase
import us.mn.state.health.domain.repository.common.UserRepository
import us.mn.state.health.model.common.*
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation
import us.mn.state.health.model.materialsrequest.RequestLineItem
import us.mn.state.health.util.Email

/**
 * Created by demita1 on 5/19/2016.
 */
class ReminderRLIApprovalToEvaluatorCommandTest extends TestCase {

    private EmailCommand sut

    /**
     * Evaluator of waiting for approval evaluation is added to mailing list
     * @throws Exception
     */
    public void test_buildTo_hasEvaluator_evaluatorAddedToMailingList() throws Exception {
        // setup
        Map<String, String> evaluators = [user1: 'APP', user2: 'WFA', user3: 'CLD']
        buildSut(evaluators, true, null)

        // when
        sut.buildTo()

        // then
        assertFalse(sut.email.to.contains("user1@email.com"))
        assertFalse(sut.email.to.contains("user3@email.com"))

        assertTrue(sut.email.to.length == 1)
        assertTrue(sut.email.to.contains("user2@email.com"))
    }

    /**
     * Evaluator group of evaluation is added to mailing list
     * @throws Exception
     */
    public void test_buildTo_noEvaluator_groupAddedToMailingList() throws Exception {
        // setup
        Map<String, String> evaluators = [user1: 'WFA', user2: 'WFA', user3: 'CLD']
        Set<String> users = ["user1", "user2", "user3", "user4"]
        buildSut(evaluators, false, buildUserRepo(users))

        // when
        sut.buildTo()

        // then
        assertTrue(sut.email.to.length == 4)
    }

    public void testEvalUrl() {
        //setup
        String expectedUrl = "<a href=\"https://prism.web.health.state.mn.us/viewEvaluateMaterialsRequest.do?requestId=1&skin=reset\">test</a>"

        //when
        String resultUrl = ReminderRLIApprovalToEvaluatorCommand.getEvalUrl(1, "test")

        //then
        assertEquals(expectedUrl, resultUrl)
    }

    private void buildSut(Map<String, String> evaluators, boolean withEvaluator, UserRepository repo) {
        sut = new ReminderRLIApprovalToEvaluatorCommand(repo, null, null, new Email(), buildRequest(evaluators, withEvaluator), 0)
    }

    private static RequestLineItem buildRequest(Map<String, String> evaluators, boolean withEvaluator) {
        RequestLineItem request = new RequestLineItem()

        Set<MaterialsRequestEvaluation> evals = new HashSet<>()
        evaluators.each { evaluator ->
            if (withEvaluator) {
                evals.add(buildEvaluation(evaluator.key, evaluator.value))
            } else {
                evals.add(buildEvaluation(null, evaluator.value))
            }
        }

        request.requestEvaluations = evals
        request
    }

    private static MaterialsRequestEvaluation buildEvaluation(String evaluator, String status) {
        MaterialsRequestEvaluation evaluation = new MaterialsRequestEvaluation()
        evaluation.with {
            evaluatorGroup = new Group()
            evaluatorGroup.groupCode = "meow"

            evaluationDecision = new Status()
            evaluationDecision.statusCode = status
        }

        if (evaluator != null) {
            evaluation.evaluator = buildPerson(evaluator + "@email.com", evaluator)
        }

        evaluation
    }

    private static Person buildPerson(String email, String username) {
        Person person = new Person()
        person.with {
            ndsUserId = username
            personEmailAddressLinks = Collections.singletonList(buildEmailLink(email))
        }
        person
    }

    private static UserRepository buildUserRepo(final Collection<String> users) {
        new UserRepository() {
            @Override
            List<User> findAllUsers() {
                return null
            }

            @Override
            User getUserById(Long userId) {
                return null
            }

            @Override
            User getUser(String username) {
                return null
            }

            @Override
            List<User> getUsersByGroupCode(String groupCode) {
                List<User> userList = new ArrayList<>()
                users.each { user ->
                    userList.add(buildUser(user + "@email.com", user))
                }
                userList
            }
        }
    }

    private static User buildUser(String email, String username) {
        User user = new User()
        user.personEmailAddressLinks = Collections.singletonList(buildEmailLink(email))
        user.username = username
        user
    }

    private static PersonEmailAddressLink buildEmailLink(String email) {
        PersonEmailAddressLink link = new PersonEmailAddressLink()
        link.with {
            emailAddress = buildEmail(email)
        }
        link
    }

    private static EmailAddress buildEmail(String email) {
        EmailAddress emailAddress = new EmailAddress()
            emailAddress.emailAddress = email
        emailAddress
    }

}
