from flask import Flask, render_template
app = Flask(__name__)

@app.route("/")
def main():
	print "yay"
    return render_template("test.html")

if __name__ == "__main__":
    app.run(debug=True)
