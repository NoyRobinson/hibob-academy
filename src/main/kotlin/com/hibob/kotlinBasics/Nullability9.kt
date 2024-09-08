package com.hibob.kotlinBasics

/**
 * Filter Departments: Identify departments that have either no manager assigned or where the manager's
 * contact information is entirely missing.
 *
 * Email List Compilation: Generate a list of all unique manager emails but exclude any null
 * or empty strings. Ensure the list has no duplicates.
 *
 * Reporting: For each department, generate a detailed report that includes the
 * department name, manager's name, email, and formatted phone number.
 * If any information is missing, provide a placeholder.
 *
 */

data class DepartmentData(val name: String?, val manager: EmployeeData?)
data class EmployeeData(val name: String?, val contactInfo: Contact?)
data class Contact(val email: String?, val phone: String?)

fun filterDepartments(departments: List<DepartmentData>): List<String?> {
    val departmentsWithManagerDataAndManagerContactInfo = managerDataAndContactInfoExists(departments)
    return createFilteredList(departmentsWithManagerDataAndManagerContactInfo)
}

fun generateEmailList(departments: List<DepartmentData>): List<String> {
    val emailList = filterEmailsNullOrEmpty(departments)
    return filterUniqueEmails(emailList)
}

fun generateReportForAllDepartments(departments: List<DepartmentData>){
    departments.map { department -> generateReport(department) }
}

private fun generateReport(department: DepartmentData) {
    val departmentName = department.name ?: "Department name missing"
    println("Department Name: $departmentName")

    department.manager?.let{
        val departmentManagersName = department.manager?.name ?: "Managers name missing"

        val departmentManagersEmail = department.manager?.contactInfo?.email?.let{
            if(department.manager.contactInfo.email == "")
                "Email missing"
            else
                department.manager.contactInfo.email
        } ?: "Email missing"

        val departmentManagersPhone = department.manager?.contactInfo?.phone ?: "Phone missing"

        println("Department managers name: $departmentManagersName")
        println("Department managers email: $departmentManagersEmail")
        println("Department managers phone number: $departmentManagersPhone")

    } ?: println("Manager info missing")

    println("----------")
    println()
}

private fun filterEmailsNullOrEmpty(departments: List<DepartmentData>): List<DepartmentData> {
    return departments.filter { department ->
        department?.manager?.let {
            department.manager.contactInfo?.let {
                department.manager.contactInfo.email?.let{
                    department.manager.contactInfo.email != ""
                } ?: false
            } ?: false
        } ?: false
    }
}

private fun filterUniqueEmails(departments: List<DepartmentData>): List<String> {
    val emails : List<String> = departments.map { department ->
        department.manager!!.contactInfo!!.email!!
    }
    return emails.distinct()
}

private fun managerDataAndContactInfoExists(departments: List<DepartmentData>): List<DepartmentData> {
    return departments.filter{ department ->
        department?.manager?.let{
            department.manager.contactInfo?.let{
                true
            } ?: false
        } ?: false
    }
}

private fun createFilteredList(departments: List<DepartmentData>): List<String?> {
    return departments.map{ department -> department.name ?: "unknown" }.toList()
}

fun main() {
    val departments = listOf(
        DepartmentData("Engineering", EmployeeData("Alice", Contact("alice@example.com", "123-456-7890"))),
        DepartmentData("Human Resources", null),
        DepartmentData("Operations", EmployeeData("Bob", Contact(null, "234-567-8901"))),
        DepartmentData("Marketing", EmployeeData(null, Contact("marketing@example.com", "345-678-9012"))),
        DepartmentData("Finance", EmployeeData("Carol", Contact("", "456-789-0123")))
    )

    // Implement the features here.
    println(filterDepartments(departments))
    println(generateEmailList(departments))
    println()
    generateReportForAllDepartments(departments)
}