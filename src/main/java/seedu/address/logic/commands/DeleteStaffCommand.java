package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PROJECT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_STAFF;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROJECT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STAFF_NAME;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.project.Project;
import seedu.address.model.project.ProjectName;
import seedu.address.model.staff.Staff;
import seedu.address.model.staff.StaffName;
import seedu.address.model.staff.UniqueStaffList;

/**
 * Delete a staff from a project.
 */
public class DeleteStaffCommand extends Command {
    public static final String COMMAND_WORD = "delstaff";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the staff from the specified project.\n"
            + "Parameters: "
            + PREFIX_PROJECT_NAME + "PROJECT_NAME "
            + PREFIX_STAFF_NAME + "STAFF_NAME\n"
            + "Example: " + COMMAND_WORD + " pn/CS2103T TP sn/Andy Lee";

    public static final String MESSAGE_DELETE_STAFF_SUCCESS = "Deleted Staff from %2$s: %1$s\n"
            + "Displaying all staff in project: %2$s ";

    private String projectName;

    private String staffName;

    /**
     * Creates a DeleteStaffCommand.
     * @param staffName the staff name to check for
     * @param projectName the project name to check for
     */
    public DeleteStaffCommand(StaffName staffName, ProjectName projectName) {
        this.staffName = staffName.staffName;
        this.projectName = projectName.fullName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Project> projectList = model.getFilteredProjectList();
        int len = projectList.size();
        Project project = null;
        for (int x = 0; x < len; x++) {
            String nameToCheck = projectList.get(x).getProjectName().toString();
            if (nameToCheck.equalsIgnoreCase(projectName)) {
                project = projectList.get(x);
                break;
            }
        }
        if (project == null) {
            throw new CommandException(String.format(MESSAGE_INVALID_PROJECT, projectName));
        }
        UniqueStaffList staffList = project.getStaffList();
        Staff staff = null;
        for (Staff tempStaff : staffList) {
            if (tempStaff.getStaffName().staffName.equalsIgnoreCase(staffName)) {
                staff = tempStaff;
                break;
            }
        }
        if (staff == null) {
            throw new CommandException(String.format(MESSAGE_INVALID_STAFF, staffName));
        }
        staffList.remove(staff);
        model.setFilteredStaffList(project);
        model.updateFilteredStaffList(Model.PREDICATE_SHOW_ALL_STAFF);
        return new CommandResult(String.format(MESSAGE_DELETE_STAFF_SUCCESS, staffName, projectName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteStaffCommand // instanceof handles nulls
                && projectName.equals(((DeleteStaffCommand) other).projectName)
                && staffName.equals(((DeleteStaffCommand) other).staffName));
    }
}
