package com.spring.leaveapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/manager")
public class ManagerController {
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	LeaveService leaveService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String manager() {
		return "manager";
	}
	
	@RequestMapping(value="/leaves",method=RequestMethod.GET)
	String manageLeaves(Model model) {
	    List<Leave> leaves = leaveService.findAllPendingLeaves();
	    if (leaves.size()==0) return "fragments/managerFrags::emptyLeaves";
        List<Object[]> leavesAndEmployees = new ArrayList<>();
        for(Leave leave:leaves){
            Employee employee = employeeService.findById(leave.getEmployeeId());
            leavesAndEmployees.add(new Object[]{leave,employee});
        }
        model.addAttribute("leavesAndEmployees",leavesAndEmployees);
        return "fragments/managerFrags::manageLeaves";
	}
	
	@RequestMapping(value="/listEmployees",method=RequestMethod.GET)
	String manageEmployees(Model model) {
		model.addAttribute("employees", employeeService.findAllEmployees());
		return "fragments/managerFrags::employees";
	}
	
	@RequestMapping(value="/addEmployee",method=RequestMethod.GET)
	String viewAddEmployeeForm() {
		return "fragments/managerFrags::addEmployee";
	}
	
	@RequestMapping(value="/addEmployee",method=RequestMethod.POST)
	String addEmployee(Employee employee) {
		employee.setRole("ROLE_USER");
		employeeService.save(employee);
		return "fragments/managerFrags::employees";
	}
	@RequestMapping(value="/deleteEmployee/{id}")
	String deleteEmployee(@PathVariable Long id, Model model) {
		employeeService.delete(id);
		model.addAttribute("employees", employeeService.findAllEmployees());
		return "fragments/managerFrags::employees";
	}
	@RequestMapping(value = "/manageLeaves")
    @ResponseBody
	void manageLeaves(@RequestParam Long employeeId, @RequestParam String action) {
	    Leave leave = leaveService.findLeaveByEmployeeId(employeeId,"Pending");
	    switch (action){
            case "Accept":
                leave.setStatus("Accepted");
                break;
            case "Reject":
                leave.setStatus("Rejected");
                break;
        }
        leaveService.updateLeave(leave);
    }
}
