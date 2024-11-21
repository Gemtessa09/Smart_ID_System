import tkinter as tk
import sqlite3
from tkinter import ttk, messagebox

# for creating database connection
conn = sqlite3.connect('restaurant.db')
c = conn.cursor()

# only creates table if table by name do not exist
c.execute("""CREATE TABLE IF NOT EXISTS orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    drink TEXT,
    food TEXT,
    service_charge REAL,
    tax REAL,
    total REAL
)""")
conn.commit()

# Define product prices
product_prices = {
    "COCA": 2.50,
    "FANTA": 3.50,
    "SPRITE": 4.6,
    "PIZZA": 10.00,
    "BURGER": 7.50,
    "FRIES": 3.00,
    "SALAD": 5.00,
 "service_charge":4.00,
 "tax_price": 3.00,
}
#For adding out order into database
def add_order():
    drink_type = drink_entry.get().upper()
    food_type = food_entry.get().upper()
    drink_type=drink_type.upper()
    food_type=food_type.upper()
    
    if not drink_type or not food_type:
        messagebox.showerror("Input Error", "Drink and Food fields cannot be empty.")
        return

    try:
        service_charge =4.00
        tax_price = 3.00
    except ValueError:
        messagebox.showerror("Input Error", "Service Charge and Tax must be valid numbers.")
        return


    total = calculate_total()
    # used for the insertion of new data into database
    c.execute("INSERT INTO orders (drink, food, service_charge, tax, total) VALUES (?, ?, ?, ?, ?)",
              (drink_type, food_type, service_charge, tax_price, total))
    conn.commit()

    # used for cleaering the entry after we add order into database
    drink_entry.delete(0, tk.END)
    food_entry.delete(0, tk.END)

    service_charge_entry.delete(0, tk.END)
    tax_entry.delete(0, tk.END)
    

    # for showing success in message form
    success_label.config(text="Order added successfully!")

    # Updating the order table
    update_order_table()

    # Updating receipt
    update_receipt(drink_type, food_type, service_charge, tax_price, total)

# to calculate total amount
def calculate_total():
    # if drink_entry.get()==""

    drink_cost = product_prices.get(drink_entry.get().upper(),0.0)# 0.0 makes it not become void
    food_cost = product_prices.get(food_entry.get().upper(),0.0)
    try:
        service_charge = float(service_charge_entry.get())
        tax = float(tax_entry.get())
    except ValueError:
        service_charge = 0.0
        tax = 0.0
    total = drink_cost + food_cost + service_charge + tax
    total_label.config(text=f"Total: {total:.2f}")
    return total

# this function is used for checking and inserting data from database in the table
def update_order_table():
    c.execute("SELECT * FROM orders")
    orders = c.fetchall()

    # Clear existing table data
    order_table.delete(*order_table.get_children())

    # fot inserting data into the table
    for order in orders:
        order_table.insert("", tk.END, values=order)


def update_receipt(drink, food, service_charge, tax, total):
    receipt_text.set(
        f"Receipt\n\nDrink: {drink}\nFood: {food}\nService Charge: {service_charge:.2f}\nTax: {tax:.2f}\nTotal: {total:.2f}")

# involved in the computation of calculator
def calculator_operation(button_text):
    current = calc_entry_var.get()
    if button_text == "=":
        try:
            result = str(eval(current))
            calc_entry_var.set(result)
        except Exception as e:
            calc_entry_var.set("Error")
    elif button_text == "C":
        calc_entry_var.set("")
    else:
        calc_entry_var.set(current + button_text)


# main window
w = tk.Tk()
w.title("Restaurant Management System")
#w.configure("red")
# Display product prices
price_label = tk.Label(w, text="Price List")
price_label.grid(row=0, column=0, columnspan=2)
price_text = ""
for item, price in product_prices.items():
    price_text += f"{item}: ${price:.2f}\n"
price_list_label = tk.Label(w, text=price_text, justify=tk.LEFT)
price_list_label.grid(row=1, column=0, columnspan=2)

# Create labels and entry fields for order
drink_label = tk.Label(w, text="Drink_type   :")
drink_label.grid(row=2, column=0, columnspan=2)
drink_entry = tk.Entry(w)
drink_entry.grid(row=2, column=1)

food_label = tk.Label(w, text="Food_type  :")
food_label.grid(row=3, column=0,columnspan=2)
food_entry = tk.Entry(w)
food_entry.grid(row=3, column=1)


service_charge_label = tk.Label(w, text="Service Charge:")
service_charge_label.grid(row=4, column=0, columnspan=2)

service_charge_entry = tk.Entry(w)
service_charge_entry.grid(row=4, column=1)

tax_label = tk.Label(w, text="Tax_price   :")
tax_label.grid(row=5, column=0, columnspan=2)

tax_entry = tk.Entry(w)
tax_entry.grid(row=5, column=1) 

tax_entry.grid(row=5, column=1)

# button called calculate
calculate_button = tk.Button(w, text="Calculate Total", command=calculate_total)
calculate_button.grid(row=7, column=0, columnspan=2 )

# place where total amount is shown
total_label = tk.Label(w, text="")
total_label.grid(row=8, column=0,)

# Create add order button
add_order_button = tk.Button(w, text="Add Order", command=add_order)
add_order_button.grid(row=8, column=1,)

# Create success label
success_label = tk.Label(w, text="")
success_label.grid(row=9, column=0, columnspan=1)

# Create a table for displaying orders
order_table = ttk.Treeview(w, columns=("ID", "Drink", "Food", "Service Charge", "Tax", "Total"), show="headings")
order_table.heading("ID", text="ID")
order_table.heading("Drink", text="Drink")
order_table.heading("Food", text="Food")
order_table.heading("Service Charge", text="Service Charge")
order_table.heading("Tax", text="Tax")
order_table.heading("Total", text="Total")
order_table.column("ID", width=50)
order_table.column("Drink", width=100)
order_table.column("Food", width=100)
order_table.column("Service Charge", width=100)
order_table.column("Tax", width=100)
order_table.column("Total", width=100)
order_table.grid(row=10, column=0, columnspan=2)

# Update the order table at the beginning
update_order_table()

# Create calculator
calc_frame = tk.Frame(w)
calc_frame.grid(row=0, column=2, rowspan=6)

calc_entry_var = tk.StringVar()
calc_entry = tk.Entry(calc_frame, textvariable=calc_entry_var, justify='right')
calc_entry.grid(row=0, column=0, columnspan=4, sticky="nsew", padx=5, pady=5)

calc_buttons = [
    '7', '8', '9', '/',
    '4', '5', '6', '*',
    '1', '2', '3', '-',
    '0', '.', '=', '+',
    'C'
]

row_val = 1
col_val = 0
for button_text in calc_buttons:
    button = tk.Button(calc_frame, text=button_text, command=lambda bt=button_text: calculator_operation(bt),bg="#F0F0F0")#pale grey
    button.grid(row=row_val, column=col_val, sticky="nsew", padx=5, pady=5)
    col_val += 1
    if col_val > 3:
        col_val = 0
        row_val += 1

# receipt section
receipt_label = tk.Label(w, text="Receipt")
receipt_label.grid(row=10, column=150, columnspan=6)

receipt_text = tk.StringVar()
receipt_display = tk.Label(w, textvariable=receipt_text, justify=tk.LEFT, relief="solid")
receipt_display.grid(row=8, column=150, columnspan=4, sticky="nsew")

# about main loop
w.geometry("800x400")
w.configure(bg="green") # light green color
w.mainloop()
#close database
conn.close()
