<%@ include file="../include/tlds.jsp" %>

<html>
  <head>
    <title>Personal Address Book</title>
  </head>
  <body>
    <html:form action="viewMaintainPersonalAddressBook" method="post">
        <table align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th class="tableheader">
                    Personal Address Book: <nested:write property="owner.firstAndLastName"/>
                </th>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <table align="center">
                        <nested:empty property="addBookPersonForm">
                            <nested:empty property="addbkExtSourceForm">
                                <tr>
                                    <td class="tablelabel">
                                        <strong>Add MDH People to your <br>
                                        Personal Address Book:</strong>
                                    </td>
                                    <td></td>
                                    <td class="tablelabel"><strong>Double-click on a Person<br> to Update:</strong></td>
                                </tr>
                                <tr>
                                    <td>
                                        <nested:notEmpty property="availableAddBkPersons">
                                            <nested:select size="4" property="availableAddBkPersonId">
                                                <nested:optionsCollection property="availableAddBkPersons" label="person.lastAndFirstName" value="addressBookPersonId"/>
                                            </nested:select>
                                        </nested:notEmpty>
                                        <nested:empty property="availableAddBkPersons">
                                            All people have<br>
                                            been chosen
                                        </nested:empty>
                                    </td>
                                    <td>
                                        <table>
                                            <tr>
                                                <td>
                                                    <nested:notEmpty property="availableAddBkPersons">
                                                        <input type="SUBMIT" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;" onclick="this.form.action='addAddressBookPerson.do'"/>
                                                    </nested:notEmpty>
                                                    <nested:empty property="availableAddBkPersons">
                                                        <input type="SUBMIT" disabled="disabled" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;" onclick="this.form.action='addAddressBookPerson.do'"/>
                                                    </nested:empty>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <nested:notEmpty property="addBkPersons">
                                                        <input type="SUBMIT" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;" onclick="this.form.action='removeAddressBookPerson.do'"/>
                                                    </nested:notEmpty>
                                                    <nested:empty property="addBkPersons">
                                                        <input type="SUBMIT" disabled="disabled" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;" onclick="this.form.action='removeAddressBookPerson.do'"/>
                                                    </nested:empty>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <nested:notEmpty property="addBkPersons">
                                            <nested:select size="4" property="persAddBkPersonId" ondblclick="this.form.action='viewEditAddressBookPerson.do';this.form.submit(); return false;">
                                                <nested:optionsCollection property="addBkPersons" label="addressBookPerson.person.lastAndFirstName" value="personalAddBkPersonId"/>
                                            </nested:select>
                                        </nested:notEmpty>
                                        <nested:empty property="addBkPersons">
                                            No people have been chosen
                                        </nested:empty>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><hr></td>
                                </tr>
                                <tr>
                                    <td class="tablelabel">
                                        <strong>Add External Delivery <br>
                                        Locations to your <br>
                                        Personal Address Book:</strong>
                                    </td>
                                    <td></td>
                                    <td class="tablelabel">
                                        <strong>Double-click a Delivery<br>Location to Update:</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <nested:notEmpty property="availableAddBkExtSources">
                                            <nested:select size="4" property="availableAddbkExtSourceId">
                                                <nested:optionsCollection property="availableAddBkExtSources" label="shortSummary" value="addressBookExternalSourceId"/>
                                            </nested:select>
                                        </nested:notEmpty>
                                        <nested:empty property="availableAddBkExtSources">
                                            All delivery locations<br>
                                            have been chosen
                                        </nested:empty>
                                    </td>
                                    <td>
                                        <table>
                                            <tr>
                                                <td>
                                                    <nested:notEmpty property="availableAddBkExtSources">
                                                        <input size="5" type="SUBMIT" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;" onclick="this.form.action='addAddressBookExtSource.do'"/>
                                                    </nested:notEmpty>
                                                    <nested:empty property="availableAddBkExtSources">
                                                        <input size="5" type="SUBMIT" disabled="disabled" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;"/>
                                                    </nested:empty>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <nested:notEmpty property="addBkExtSources">
                                                        <input size="5" type="SUBMIT" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;" onclick="this.form.action='removeAddressBookExtSource.do'"/>
                                                    </nested:notEmpty>
                                                    <nested:empty property="addBkExtSources">
                                                        <input size="5" type="SUBMIT" disabled="disabled" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;"/>
                                                    </nested:empty>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <nested:notEmpty property="addBkExtSources">
                                            <nested:select size="4" property="personalAddBkExtSourceId" ondblclick="this.form.action='viewEditAddressBookExtSource.do';this.form.submit(); return false;">
                                                <nested:optionsCollection property="addBkExtSources" label="addBkExtSource.shortSummary" value="personalAddBkExtSourceId"/>
                                            </nested:select>
                                        </nested:notEmpty>
                                        <nested:empty property="addBkExtSources">
                                            No delivery locations<br> have been chosen
                                        </nested:empty>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td>
                                        <a href="viewNewAddressBookExtSource.do">Add New Delivery Location</a>
                                    </td>
                                </tr>
                            </nested:empty>
                        </nested:empty>
                            
                        <nested:notEmpty property="addBookPersonForm">    
                            <tr>
                                <td class="tablelabel" colspan="3" align="center"><strong>Edit Address Book Person</strong></td>
                            </tr>
                            <tr>
                                <td class="tablelabel" align="right">Person:</td>
                                <td colspan="2">
                                    <nested:write property="addBookPersonForm.person.lastAndFirstName"/>
                                </td>
                                
                            </tr>
                            <tr>
                                <td class="tablelabel" align="right">Location:</td>
                                    <td colspan="2">
                                        <nested:notEmpty property="addBookPersonForm.person.primaryPosition">
                                            <nested:select property="addBookPersonForm.facilityId">
                                                <option label="" value=""/>
                                                <nested:optionsCollection property="addBookPersonForm.facilities" label="facilityName" value="facilityId"/>
                                            </nested:select>
                                        </nested:notEmpty>
                                        <nested:empty property="addBookPersonForm.person.primaryPosition">
                                            Facility cannot be assigned to this person. Please contact HRM<br>
                                            to have this person assigned to a position.
                                        </nested:empty>
                                    </td>
                            </tr>
                            <tr>
                                <td colspan="3">&nbsp;</td>
                            </tr>
                            <tr>
                              <td colspan="3" align="center">
                                <input type="SUBMIT" value="Cancel" onclick="this.form.action='viewMaintainPersonalAddressBook.do'"/>
                                <nested:notEmpty property="addBookPersonForm.person.primaryPosition">
                                    &nbsp;&nbsp;<input type="SUBMIT" value="Save" onclick="this.form.action='saveAddressBookPerson.do'"/>
                                </nested:notEmpty>
                              </td>
                              <td></td>
                            </tr>
                        </nested:notEmpty>
                        
                        <nested:notEmpty property="addbkExtSourceForm">
                            <tr>
                                <td class="tablelabel" colspan="3" align="center">
                                    <nested:notEmpty property="addbkExtSourceForm.addressBookExternalSourceId">
                                        <strong>Edit Address Book Delivery Location:</strong>
                                    </nested:notEmpty>
                                    <nested:empty property="addbkExtSourceForm.addressBookExternalSourceId">
                                        <strong>New Address Book Delivery Location:</strong>
                                    </nested:empty>
                                </td>
                            </tr>
                            <nested:equal property="addbkExtSourceForm.contactOnly" value="false">
                                <tr>
                                    <td align="right" class="tablelabel" nowrap="nowrap">Organization Name
                                       <nested:empty property="addbkExtSourceForm.orgId">&nbsp;(Optional)</nested:empty>
                                    </td>
                                    <td colspan="2">
                                        <nested:text size="30" property="addbkExtSourceForm.orgName"/>
                                    </td>
                                </tr>
                            </nested:equal>
                            <tr>
                                <td align="right" class="tablelabel" nowrap="nowrap">Person's First Name</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.firstName"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="tablelabel" nowrap="nowrap">Person's Last Name</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.lastName"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="tablelabel" nowrap="nowrap">Street Address</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.streetAddress"/>
                                </td>
                            </tr>
                            <tr>
                                <td  align="right" class="tablelabel" nowrap="nowrap">City</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.city"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="tablelabel" nowrap="nowrap">State</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.state"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" class="tablelabel" nowrap="nowrap">Zip</td>
                                <td colspan="2">
                                    <nested:text size="30" property="addbkExtSourceForm.zip"/>
                                </td>
                            </tr>
                            <tr>
                              <td colspan="3" align="center">
                                <input type="SUBMIT" value="Cancel" onclick="this.form.action='viewMaintainPersonalAddressBook.do'"/>&nbsp;&nbsp;
                                <input type="SUBMIT" value="Save" onclick="this.form.action='saveAddressBookExtSource.do'"/>
                              </td>
                            </tr>
                        </nested:notEmpty>
                    </table>
                </td>
            </tr>
        </table>
    </html:form>
  </body>
</html>
