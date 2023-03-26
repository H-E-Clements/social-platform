let numLines = 1;
let currentLine = 0;

// characters used to identify variable in expression
const regex = /[a-zA-Z%]/;

const expression = '5+    9';
const result = evaluateExpression(expression);
console.log(`The result of ${expression} is ${result}`);

// function evaluates mathematical expression from string
function stringMath(eq, callback) {
  if (typeof eq !== 'string') return handleCallback(new TypeError('The [String] argument is expected.'), null);
  const mulDiv = /([+-]?\d*\.?\d+(?:e[+-]\d+)?)\s*([*/])\s*([+-]?\d*\.?\d+(?:e[+-]\d+)?)/;
  const plusMin = /([+-]?\d*\.?\d+(?:e[+-]\d+)?)\s*([+-])\s*([+-]?\d*\.?\d+(?:e[+-]\d+)?)/;
  const parentheses = /(\d)?\s*\(([^()]*)\)\s*/;
  var current;
  while (eq.search(/^\s*([+-]?\d*\.?\d+(?:e[+-]\d+)?)\s*$/) === -1) {
    eq = fParentheses(eq);
    if (eq === current) return handleCallback(new SyntaxError('The equation is invalid.'), null);
    current = eq;
  }
  return handleCallback(null, +eq);

  function fParentheses(eq) {
    while (eq.search(parentheses) !== -1) {
      eq = eq.replace(parentheses, function (a, b, c) {
        c = fMulDiv(c);
        c = fPlusMin(c);
        return typeof b === 'string' ? b + '*' + c : c;
      });
    }
    eq = fMulDiv(eq);
    eq = fPlusMin(eq);
    return eq;
  }

  function fMulDiv(eq) {
    while (eq.search(mulDiv) !== -1) {
      eq = eq.replace(mulDiv, function (a) {
        const sides = mulDiv.exec(a);
        const result = sides[2] === '*' ? sides[1] * sides[3] : sides[1] / sides[3];
        return result >= 0 ? '+' + result : result;
      });
    }
    return eq;
  }

  function fPlusMin(eq) {
    eq = eq.replace(/([+-])([+-])(\d|\.)/g, function (a, b, c, d) { return (b === c ? '+' : '-') + d; });
    while (eq.search(plusMin) !== -1) {
      eq = eq.replace(plusMin, function (a) {
        const sides = plusMin.exec(a);
        return sides[2] === '+' ? +sides[1] + +sides[3] : sides[1] - sides[3];
      });
    }
    return eq;
  }

  function handleCallback(errObject, result) {
    if (typeof callback !== 'function') {
      if (errObject !== null) throw errObject;
    } else {
      callback(errObject, result);
    }
    return result;

  }

}

// helper function for stringMath()
function evaluateExpression(expression) {
  try {
    return stringMath(expression);
  } catch (error) {
    console.log(`Error evaluating expression: ${error.message}`);
    return null;
  }
}


optionValues = [{name: "x", value: 0, stack: []}, {name: "y", value: 1, stack: []}, {name: "z", value: 0, stack: []}]

const addLine = () => {

    // get the parent element to which the line divs will be appended
    const parentElem = document.getElementById("code");
  
      // create the line div
      const lineDiv = document.createElement("div");
      lineDiv.classList.add("line");
      lineDiv.id = "line" + numLines;
  
      // create the select element
      const selectElem = document.createElement("select");
      selectElem.id = "line"+numLines+"Select";

      // add options to the select element
      for (let j = 0; j < optionValues.length; j++) {
        const optionElem = document.createElement("option");

        optionElem.value = optionValues[j].name;
        optionElem.textContent = optionValues[j].name;
        selectElem.appendChild(optionElem);
      }
  
      // create the p element
      const pElem = document.createElement("p");
      pElem.textContent = "=";
  
      // create the input element
      const inputElem = document.createElement("input");
      inputElem.type = "text";
      inputElem.id = "line" + numLines + "Text";
  
      // append the elements to the line div
      lineDiv.appendChild(selectElem);
      lineDiv.appendChild(pElem);
      lineDiv.appendChild(inputElem);
  
      // append the line div to the parent element
      parentElem.appendChild(lineDiv);

    //increment numlines
    numLines+=1
    
  }

  function updateCurrentState() {
    // get the parent element to which the p elements will be appended
    const parentElem = document.getElementById("currentState");
    const stacks = document.getElementById("stackLists");

    // remove all child elements from the parent element
        while (parentElem.firstChild) {
            parentElem.removeChild(parentElem.firstChild);
        }
        if (stacks) {
          stacks.innerHTML = "";
        }
        
    
    
    // update stack element
    for (let i = 0; i < optionValues.length; i++) {
      const option = optionValues[i];
      const list = document.createElement("ul");
      const title = document.createElement('h3');

      // set header of list to variable name
      title.textContent = option.name
      list.appendChild(title)

      // loop through varable stack and append to list
      for (let j = 0; j < option.stack.length; j++) {
        const item = document.createElement('li')
        item.textContent = option.stack[j];
        list.appendChild(item);
      }
      stacks.appendChild(list);
    }

  
    // loop through the array and create a p element for each object
    for (let i = 0; i < optionValues.length; i++) {
      // create the p element
      const pElem = document.createElement("p");
      pElem.textContent = optionValues[i].name + " = " + optionValues[i].value;
  
      // append the p element to the parent element
      parentElem.appendChild(pElem);
    }
  }

  const executeLine = () => {
    lineDiv = document.getElementById("line"+currentLine);
    varName = document.getElementById("line"+currentLine+"Select").value;
    lineText = document.getElementById("line"+currentLine+"Text").value;
    for (var i=0; i<optionValues.length; i++) {
      if (optionValues[i].name == varName) {
        varIndex = i;
        break;
      }
    };
    if (regex.test(lineText)) {
      let newExpression = lineText; 
      // replace variable name with variable value within expression
      optionValues.forEach(element => {
        newExpression = newExpression.replace('%'+element.name, element.value)
      });
      console.log(newExpression);
      optionValues[varIndex].stack.push(evaluateExpression(newExpression));
      optionValues[varIndex].value = evaluateExpression(newExpression);
    } else {
      optionValues[varIndex].stack.push(optionValues[varIndex].value);
      optionValues[varIndex].value = lineText;
    }
    updateCurrentState();
  }

  const next = () => {
    if (currentLine < numLines) {
      try{
        executeLine();
        document.getElementById("line"+currentLine+"Text").disabled = true;
        document.getElementById("line"+currentLine+"Select").disabled = true;
        if (currentLine < numLines) {
          lineDiv = document.getElementById("line"+currentLine);
          lineDiv.classList.add("highlighted");
        }
        currentLine += 1;
      } catch (e) {
        console.error(e);
      }
    }

  }

  const previous = () => {
      const prevLine = currentLine - 1;
      const lineDiv = document.getElementById("line"+prevLine);
      const varName = document.getElementById("line"+prevLine+"Select");
      const lineText = document.getElementById("line"+prevLine+"Text");
      for (var i=0; i<optionValues.length; i++) {
        if (optionValues[i].name == varName.value) {
          varIndex = i;
          break;
        }
      };
      optionValues[varIndex].value = optionValues[varIndex].stack.pop()
      updateCurrentState();
      document.getElementById("line"+prevLine+"Text").disabled = false;
      document.getElementById("line"+prevLine+"Select").disabled = false;
      if (currentLine > 0) {
      lineDiv.classList.remove("highlighted");
      }
      currentLine -= 1;
  }

  const setVariables = () => {
    optionValues.forEach(element => {
      element.value = document.getElementById("var"+element.name).value;
    });
    updateCurrentState();
    currentLine = 0;
    let code = document.getElementById('code').children;
    for (var i = 0; i < code.length; i++) {
      var tableChild = code[i];
      tableChild.classList.remove("highlighted");
    }
    document.getElementById("line0").classList.add("highlighted");
  }