from tkinter import*
import tkinter
from tkinter import messagebox
from tkinter.ttk import *
import sqlite3
win=Tk()
win.title("Registration Form")
win.geometry('500x500')
win.configure(bg='#D3D3D3')

def Register_Form():
    connection = sqlite3.connect("HrU.db")
    if connection==0:
       connection.execute("create table STUDENT_INFO"
                       "(Student_Name text primary key not null,"
                       "College text not null,"
                       "Email text not null,"
                       "Phone text not null,"
                       "Gender text not null,"
                       "Department text not null)")
    stud_name = e1.get()
    college = e2.get()
    email = e3.get()
    phone = e5.get()
    gender = radio_var.get()
    dept = combo.get()
    connection.execute("insert into STUDENT_INFO(Student_Name,College,Email,Phone,Gender,Department) values(?,?,?,?,?,?)",(stud_name, college, email, phone,gender,dept))
    connection.commit()
    connection.close()
    messagebox.showinfo('RESULT', "Data is inserted Successfully")

def Show_data():
    c= ''
    connection = sqlite3.connect("HrU.db")
    row=connection.execute("select * from STUDENT_INFO")
    for a in row.fetchall():
        l_show.configure(text=a)



label_of_stud = Label(win, text='STUDENT REGISTRATION FORM', font=('Times New Roman bold', 20))
label_of_stud.pack(fill=X)
l1=Label(win,text="Name",font=('Arial Bold',10)).place(x=20,y=50)
e1=Entry(win,width=30)
e1.place(x=150,y=50)
l2=Label(win,text="College",font=('Arial Bold',10)).place(x=20,y=80)
e2=Entry(win,width=30)
e2.place(x=150,y=80)
l3=Label(win,text="Email Address",font=('Arial Bold',10)).place(x=20,y=110)
e3=Entry(win,width=30)
e3.place(x=150,y=110)
l5=Label(win,text="Phone Number",font=('Arial Bold',10)).place(x=20,y=150)
e5=Entry(win,width=30)
e5.place(x=150,y=150)

l4=Label(win,text="Select Gender",font=('Arial Bold',10)).place(x=20,y=180)

label_of_frame = LabelFrame(win,text='',borderwidth=5).pack(side=LEFT, fill=BOTH)

radio_var = StringVar(value='')

GenderFrame = Frame(label_of_frame).place(x=150,y=180)
male_radio = Radiobutton(GenderFrame, text="MALE", variable=radio_var, value='MALE').place(x=150,y=180)
female_radio = Radiobutton(GenderFrame, text="FEMALE", variable=radio_var, value='FEMALE').place(x=230,y=180)
other_radio = Radiobutton(GenderFrame, text="OTHERS", variable=radio_var, value='OTHERS').place(x=320,y=180)

l5=Label(win,text="Department",font=('Arial Bold',10)).place(x=20,y=210)
combo=Combobox(win,width=30,font=('Arial Bold',10))
combo['values']=("Software Engineering","Computer Science","Information Technology","Information Systems")
combo.current(0)
combo.place(x=150,y=210)
btn=tkinter.Button(win,text="Register",command=Register_Form,width=20,bg="green",fg="white",font=('Arial Bold',12))
btn.place(x=130,y=250)
btn_show=tkinter.Button(win,text="Show",command=Show_data,width=20,bg="green",fg="white",font=('Arial Bold',12))
btn_show.place(x=130,y=300)

l_show=Label(win,text='',font=('Arial Bold',10))
l_show.place(x=10,y=400)
win.mainloop()
