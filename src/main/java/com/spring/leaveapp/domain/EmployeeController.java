package com.spring.leaveapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@RequestMapping(value="/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    LeaveService leaveService;

	@RequestMapping(method=RequestMethod.GET)
	public String employee(String employeeId) {
		return "employee";
	}

	@RequestMapping(value = "/leave-status")
	public String leaveStatus(Model model, Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        Leave leave;
        try {
            leave = leaveService.findActiveLeaveByEmployeeId(employee.getId());
        }
        catch (EmptyResultDataAccessException e){
            return "fragments/employeeFrags::noLeave";
        }
        model.addAttribute("leave",leave);
        return "fragments/employeeFrags::leaveStatus";
	}

	@RequestMapping(value = "/apply-leave", method = RequestMethod.GET)
    public String applyLeave(Principal principal) throws Exception{
        Employee employee = employeeService.findByEmail(principal.getName());
        try {
            leaveService.findLeaveByEmployeeId(employee.getId(), "Pending");
        }
        catch (EmptyResultDataAccessException e){
            return "fragments/employeeFrags::applyLeave";
        }
        return "fragments/employeeFrags::leavePendingAlert";

	}

    @RequestMapping(value = "/apply-leave", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveLeave(Leave leave, Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        leave.setStatus("Pending");
        leave.setEmployeeId(employee.getId());
        leaveService.saveLeave(leave);
    }

    @RequestMapping(value = "/view-leaves")
    public String viewLeaves(Model model, Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        model.addAttribute("leaves",leaveService.findArchivedLeavesByEmployeeId(employee.getId()));
        return "fragments/employeeFrags::leaveListForEmployee";
    }

    @RequestMapping(value = "/cancel-pending-leave")
    public String cancelPendingLeave(Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        Leave leave = leaveService.findLeaveByEmployeeId(employee.getId(), "Pending");
        leaveService.deleteLeave(leave.getId());
        return "fragments/employeeFrags::noLeave";
    }

    @RequestMapping(value = "/archive-accepted-leave")
    @ResponseBody
    public void archiveAcceptedLeave(Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        Leave leave = leaveService.findLeaveByEmployeeId(employee.getId(), "Accepted");
        leave.setStatus("Archived");
        leaveService.updateLeave(leave);
    }

    @RequestMapping(value = "/delete-rejected-leave")
    @ResponseBody
    public void deleteRejectedLeave(Principal principal){
        Employee employee = employeeService.findByEmail(principal.getName());
        Leave leave = leaveService.findLeaveByEmployeeId(employee.getId(), "Rejected");
        leaveService.deleteLeave(leave.getId());
    }
}
