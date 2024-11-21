import tkinter as tk
from tkinter import *
from tkinter.messagebox import showinfo
import sqlite3

# Initialize the main window
root = Tk()
root['bg']='green'
#root.resizable(0, 0)
root.title("Student Registration Form")

# Define StringVar variables to hold input data
Fullname = StringVar()
Email = StringVar()
Gender = StringVar()
Branch = StringVar()
Programming = [StringVar(), StringVar(), StringVar()]

# Create the database table if it doesn't exist
def create_table():
    with sqlite3.connect('students.db') as conn:
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS Student (
                Fullname TEXT,
                Email TEXT,
                Gender TEXT,
                Departement TEXT,
                Programming TEXT
            )
        ''')
        conn.commit()

# Function to insert data into the database
def insert_data():
    name = Fullname.get()
    email = Email.get()
    gender = Gender.get()
    branch = Branch.get()
    prog = ",".join(filter(None, [Programming[0].get(), Programming[1].get(), Programming[2].get()]))
    
    with sqlite3.connect('students.db') as conn:
        cursor = conn.cursor()
        cursor.execute('''
            INSERT INTO Student (Fullname, Email, Gender, Branch, Programming)
            VALUES (?, ?, ?, ?, ?)
        ''', (name, email, gender, branch, prog))
        conn.commit()
        
    showinfo(title="Student Record", message="Data inserted successfully")

# Function to display data from the database
def display_data():
    with sqlite3.connect('students.db') as conn:
        cursor = conn.cursor()
        cursor.execute('SELECT * FROM Student')
        data = cursor.fetchall()
    
    output = '\n' "Full Name:" .join([f"{x[0]} \n Email: {x[1]}  \n Gender:  {x[2]} \n Branch: {x[3]} \n programming: {x[4]}" for x in data])
    text.delete(1.0, END)
    text.insert(END, output)

# Function to delete a record from the database
def delete_data():
    name = Fullname.get()
    
    with sqlite3.connect('students.db') as conn:
        cursor = conn.cursor()
        cursor.execute('DELETE FROM Student WHERE Fullname = ?', (name,))
        conn.commit()
        
    showinfo(title="Student Record", message="Data deleted successfully")

# Function to update a record in the database
def update_data():
    name = Fullname.get()
    email = Email.get()
    gender = Gender.get()
    branch = Branch.get()
    prog = ",".join(filter(None, [Programming[0].get(), Programming[1].get(), Programming[2].get()]))
    
    with sqlite3.connect('students.db') as conn:
        cursor = conn.cursor()
        cursor.execute('''
            UPDATE Student SET Email=?, Gender=?, Branch=?, Programming=?
            WHERE Fullname = ?
        ''', (email, gender, branch, prog, name))
        conn.commit()
        
    showinfo(title="Student Record", message="Data updated successfully")

# Function to exit the application
def exit_application():
    if tk.messagebox.askyesno("Student Registration Form", "Do you want to exit?"):
        root.destroy()

# Create the user interface
canvas1 = tk.Canvas(root, width=1000, height=500, relief='raised', bg="white")
canvas1.pack()

label1 = tk.Label(root, text='Registration Form')
label1.config(font=("bold", 18), bg="yellow")
canvas1.create_window(250, 30, window=label1)

label2 = tk.Label(root, text='Fullname :')
label2.config(font=('helvetica', 14), bg="green")
canvas1.create_window(65, 90, window=label2)

entry1 = tk.Entry(root, textvar=Fullname, font=(14), borderwidth=2, width=30)
canvas1.create_window(320, 90, window=entry1)

label3 = tk.Label(root, text='E-mail :')
label3.config(font=('helvetica', 14), bg="green")
canvas1.create_window(65, 140, window=label3)

entry2 = tk.Entry(root, textvar=Email, font=(14), borderwidth=2, width=30)
canvas1.create_window(320, 140, window=entry2)

label4 = tk.Label(root, text='Gender :')
label4.config(font=('helvetica', 14), bg="green")
canvas1.create_window(65, 190, window=label4)

rd1 = tk.Radiobutton(root, text="Male", padx=5, variable=Gender, value="Male")
rd1.config(font=('helvetica', 14), bg="green")
canvas1.create_window(200, 190, window=rd1)

rd2 = tk.Radiobutton(root, text="Female", padx=20, variable=Gender, value="Female")
rd2.config(font=('helvetica', 14), bg="green")
canvas1.create_window(300, 190, window=rd2)

label5 = tk.Label(root, text='Departement :')
label5.config(font=('helvetica', 14), bg="yellow")
canvas1.create_window(65, 240, window=label5)

list1 = ['CS', 'SOFTWARE', 'CIVIL', 'TI']
droplist = tk.OptionMenu(root, Branch, *list1)
droplist.config(font=('helvetica', 14), bg="yellow", width=27)
Branch.set('Select your Departement')
canvas1.create_window(320, 240, window=droplist)

label6 = tk.Label(root, text='Programming :')
label6.config(font=('helvetica', 14), bg="yellow")
canvas1.create_window(65, 290, window=label6)

cb1 = tk.Checkbutton(root, text="Python", variable=Programming[0], onvalue="Python", offvalue="")
cb1.config(font=('helvetica', 14), bg="yellow")
canvas1.create_window(200, 290, window=cb1)

cb2 = tk.Checkbutton(root, text="Java", variable=Programming[1], onvalue="Java", offvalue="")
cb2.config(font=('helvetica', 14), bg="yellow")
canvas1.create_window(300, 290, window=cb2)

cb3 = tk.Checkbutton(root, text="C", variable=Programming[2], onvalue="C", offvalue="")
cb3.config(font=('helvetica', 14), bg="yellow")
canvas1.create_window(400, 290, window=cb3)

button1 = tk.Button(text='   Submit   ', command=insert_data, bg='black', fg='white', font=('helvetica', 12, 'bold'))
canvas1.create_window(150, 350, window=button1)

button2 = tk.Button(text='   Display   ', command=display_data, bg='black', fg='white', font=('helvetica', 12, 'bold'))
canvas1.create_window(300, 350, window=button2)

button3 = tk.Button(text='   Update   ', command=update_data, bg='black', fg='white', font=('helvetica', 12, 'bold'))
canvas1.create_window(150, 450, window=button3)

button4 = tk.Button(text='   Delete   ', command=delete_data, bg='black', fg='white', font=('helvetica', 12, 'bold'))
canvas1.create_window(300, 450, window=button4)

text = tk.Text(root, height=25, width=50)
text.config(font=('helvetica', 12), bg="yellow")
canvas1.create_window(750, 270, window=text)

lblDisplay = tk.Label(root, text="Student Data")
lblDisplay.config(font=('Helvetica', 18, 'bold'), fg='black', justify=CENTER, bg="yellow")
canvas1.create_window(750, 25, window=lblDisplay)

# Menu setup
menubar = Menu(root)
filemenu = Menu(menubar, tearoff=0)
menubar.add_cascade(label='File', menu=filemenu)
filemenu.add_command(label="Exit", command=exit_application)
root.config(menu=menubar)

# Create the table and run the application
create_table()
root.mainloop()
