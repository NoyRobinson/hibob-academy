package com.hibob.nullability

/**
 * Instructions:
 *
 * Traverse through the company structure starting from departments to teams and finally to team members.
 * For each level (company, department, team, leader, members), check for null values and print appropriate information.
 * Ensure that every piece of information printed includes a fallback for null values using the Elvis operator and that
 * blocks of code dependent on non-null values are executed using ?.let.
 *
 */
data class Company(val name: String?, val departments: List<DepartmentDetails?>?)
data class DepartmentDetails(val name: String?, val teams: List<Team?>?)
data class Team(val name: String?, val leader: Leader?, val members: List<Member?>?)
data class Leader(val name: String?, val title: String?)
data class Member(val name: String?, val role: String?)

fun initializeCompany(): Company {
    return Company(
        "Tech Innovations Inc.",
        listOf(
            DepartmentDetails("Engineering", listOf(
                Team("Development", Leader("Alice Johnson", "Senior Engineer"), listOf(Member("Bob Smith", "Developer"), null)),
                Team("QA", Leader(null, "Head of QA"), listOf(Member(null, "QA Analyst"), Member("Eve Davis", null))),
                null
            )),
            DepartmentDetails(null, listOf(
                Team("Operations", null, listOf(Member("John Doe", "Operator"), Member("Jane Roe", "Supervisor")))
            )),
            null
        )
    )
}

//fun main() {
//    val company = initializeCompany()
//
//    // Task: Print detailed information about each department, team, and team members, handling all null values appropriately.
//
//    val companyName = company.name ?: "Company name not found"
//    println("company name: $companyName")
//
//    println()
//
//    company.departments?.let{
//
//        for(department in company.departments){
//
//            department?.let{
//                val departmentName: String = department.name ?: "Department name not found"
//                println("Department name: $departmentName")
//
//                department.teams?.let{
//                    for(team in department.teams){
//                        println("Team details:")
//
//                        team?.let {
//                            val teamName: String = team.name ?: "Team name not found"
//                            println("   name: $teamName")
//                            println("   Team leader details:")
//
//                            team.leader?.let{
//                                val teamLeaderName: String = team.leader.name ?: "Leader name not found"
//                                val teamLeaderTitle: String = team.leader.title ?: "Leader title not found"
//                                print("      name: $teamLeaderName  ")
//                                println("title: $teamLeaderTitle")
//                            } ?: println("     Team leader not found")
//
//                            println("   Team members details:")
//
//                            team.members?.let{
//                                for(member in team.members){
//                                    member?.let {
//                                        val teamMemberName: String = member.name ?: "Member name not found"
//                                        val teamMemberRole: String = member.role ?: "Member role not found"
//                                        print("      name: $teamMemberName  ")
//                                        println("role: $teamMemberRole")
//                                    } ?: println("      Member not found")
//                                }
//                            } ?: println("   Team members not found")
//                        } ?: println("   Team not found")
//
//                        println()
//                    }
//                } ?: print("Department teams not found")
//            } ?: println("Department not found")
//        }
//    } ?: println("Company departments information not found")
//}
