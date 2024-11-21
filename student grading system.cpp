#include <iostream>
#include <string>

using namespace std; 

// Gemtessa Deksisa Tolera.

  int main() 
{
	system("color 2f");
    string name, department;
	 char gender;
    int id,numbercourse;
    string coursename,creditedhours;
    
    char grade;
    float totalmarks;
      float midExam, assignment, labAssignment, finalExam;
      
     double totalCGPA = 0;
     
cout<<" WELLCOME TO STUDENT GRADING SYSTEM"<<endl ;


      cout << "Enter Student's ful Name: ";
    getline(cin, name);

      cout << "Enter Student's ID: ";
      cin >> id;
       cout<<"Enter gender(M/F):";
      cin>>gender;
     cin.ignore();
    

     for (int course = 1; course <=4; course++)
     {
        cout<<"Enter course  name : "<<course;
    
         cin>>coursename;
         
    //cout<<" Enters credited hours of  "<<coursename<<":";
   //cin>>creditedhours;   
    


            //cout << "Enter marks for  " <<    coursename<<endl;        

       cout << "Enter Mid-exam out of 30 for  " <<coursename<<":";
       cin>>midExam;
       
       cout<< "assignment out of 10 for  " <<coursename<<":";
        cin >> assignment;
       
     cout << "Lab-Assignment out of 10 for     " <<coursename<<" :";
        cin >> labAssignment;
        
      cout << "Final Exam out of 50 for        "<<coursename<<":";
      
        cin >> finalExam;
              
          char grade;
        float  totalMarks = (midExam + assignment + labAssignment + finalExam);
   
          double cgp = 0;

        if (totalMarks >= 80) {
        cout<<"totalMarks:="<<totalMarks<<endl;    
           
   cout<<"grade=A"<<endl;
            cgp = 4.0; }
        else if (totalMarks >= 70) {
                    cout<<"totalMarks:="<<totalMarks<<endl;   
             cout<<"grade =B"<<endl;
            cgp = 3.0;
        } else if (totalMarks >= 50) {
                    cout<<"totalMarks:="<<totalMarks<<endl;
         
         cout<<"grade =C"<<endl;
            cgp = 2.0;
        } else if (totalMarks >= 40) {
         cout<<"totalMarks:="<<totalMarks<<endl;   
       cout<<"grade =D"<<endl;
             cgp = 1.0;
        } else {
                  cout<<"totalMarks:="<<totalMarks<<endl;      
        cout<<"grade =F"<<endl;
             cgp = 0.0;
        }
        grade;
        totalCGPA += cgp;
    }
   
    cout << "Student Information:" << endl;
    cout << "Name: " << name << endl;
    cout << "ID: " << id << endl;
  cout<<"gender:"<<gender<<endl;
  
   //cout<<"GRADE:"<<grade<<endl;
    cout << "CGPA: " << totalCGPA / 4 << endl;

    return 0;
}

