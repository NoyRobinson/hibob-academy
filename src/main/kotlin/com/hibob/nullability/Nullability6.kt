package com.hibob.nullability

data class Department(val name: String?, val manager: EmployeeDetails?)
data class EmployeeDetails(val name: String?, val contactInfo: ContactInfo?)
data class ContactInfo(val email: String?, val phone: String?)

//fun main() {
//    val departments = listOf(
//        Department("Engineering", EmployeeDetails("Alice", ContactInfo("alice@example.com", null))),
//        Department("Human Resources", null),
//        Department(null, EmployeeDetails("Bob", ContactInfo(null, "123-456-7890"))),
//        Department("Marketing", EmployeeDetails(null, ContactInfo("marketing@example.com", "987-654-3210")))
//    )
//
//    // Task: Print each department's name and manager's contact information.
//    // If any information is missing, use appropriate defaults.
//
//
//    for(department in departments) {
//        department?.name?.let{
//            println("Department name: ${department.name}")
//        } ?: println("Information of department name is missing")
//
//        department?.manager?.let {
//            println("Department manager's information:")
//
//            department.manager.name?.let{
//                println("   name: ${department.manager.name}")
//            } ?: println("   Information of department manager's name is missing")
//
//            department.manager.contactInfo?.let{
//
//                department.manager.contactInfo.email?.let{
//                    println("   email: ${department.manager.contactInfo.email}")
//                } ?: println("   Information of department manager's email is missing")
//
//                department.manager.contactInfo.phone?.let{
//                    println("   phone: ${department.manager.contactInfo.phone}")
//                } ?: println("   Information of department manager's phone is missing")
//
//            } ?: println("   Information of department manager's contact info is missing")
//
//        } ?: println("   Information of department manager is missing")
//        println()
//        println("---------------------")
//        println()
//    }
//}