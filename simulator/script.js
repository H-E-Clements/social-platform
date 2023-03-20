let numLines = 1;
let currentLine = 0;

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
    optionValues[varIndex].stack.push(optionValues[varIndex].value);
    optionValues[varIndex].value = lineText;
    updateCurrentState();
  }

  const next = () => {
    if (currentLine < numLines) {
      try{
        executeLine();
        document.getElementById("line"+currentLine+"Text").disabled = true;
        document.getElementById("line"+currentLine+"Select").disabled = true;
        currentLine += 1;
        lineDiv = document.getElementById("line"+currentLine);
        lineDiv.classList.add("highlighted");
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