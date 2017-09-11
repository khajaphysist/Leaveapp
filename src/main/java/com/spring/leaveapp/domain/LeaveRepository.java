package com.spring.leaveapp.domain;

import java.util.List;

public interface LeaveRepository {
    public void saveLeave(Leave leave);
    public List<Leave> findAllPendingLeaves();
    public Leave findLeaveByEmployeeId(Long employeeId, String status);
    public List<Leave> findArchivedLeavesByEmployeeId(Long employeeId);
    public void deleteLeave(Long leaveId);
    public Leave findActiveLeaveByEmployeeId(Long employeeId);
    public void updateLeave(Leave leave);
}
