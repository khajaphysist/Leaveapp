package com.spring.leaveapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {
    @Autowired
    LeaveRepository leaveRepository;

    public void saveLeave(Leave leave){
        leaveRepository.saveLeave(leave);
    }

    public void deleteLeave(Long leaveId){
        leaveRepository.deleteLeave(leaveId);
    }

    public void updateLeave(Leave leave){
        leaveRepository.updateLeave(leave);
    }

    public List<Leave> findAllPendingLeaves(){
        return leaveRepository.findAllPendingLeaves();
    }

    public Leave findLeaveByEmployeeId(Long employeeId, String status){
        return leaveRepository.findLeaveByEmployeeId(employeeId, status);
    }

    public List<Leave> findArchivedLeavesByEmployeeId(Long employeeId){
        return leaveRepository.findArchivedLeavesByEmployeeId(employeeId);
    }

    public Leave findActiveLeaveByEmployeeId(Long employeeId){
        return leaveRepository.findActiveLeaveByEmployeeId(employeeId);
    }
}
