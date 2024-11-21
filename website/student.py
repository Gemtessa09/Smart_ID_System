def calculate_grade(score, attendance):
    try:
        attendance = float(attendance)
        if 0 <= attendance <= 100:
            if attendance < 75:
                grade = "Fail"
            else:
                if score >= 90:
                    grade = "A"
                elif 80 <= score <= 89:
                    grade = "B"
                elif 70 <= score <= 79:
                    grade = "C"
                elif 60 <= score <= 69:
                    grade = "D"
                else:
                    grade = "Fail"
        else:
            raise ValueError("Invalid attendance percentage. Please enter a value between 0 and 100.")
    except ValueError as e:
        grade = "Error: " + str(e)

    return grade

# Example usage:
score_input = float(input("Enter the student's score: "))
attendance_input = input("Enter the student's attendance percentage: ")

result = calculate_grade(score_input, attendance_input)
print("The student's grade is:", result)