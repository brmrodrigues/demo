package com.example.demo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "api/v1/simian")
public class TaskController extends BaseController {
  // @Autowired
  // private TaskRepository;

  // @PostMapping(value="")
  // public ResponseEntity<ResponseObject> createTask(
  //   @RequestBody @Valid @NotNull TaskRequest taskRequest) {
  //     if (taskRequest.getName() == null || taskRequest.getName().isEmpty()) {
  //       return erro("Por favor, informe o nome");
  //     } else if (taskRequest.getName().equals("Bruno")) {
  //       return ok("Nome Correto!");
  //     } else {
  //       return erro("Nome incorreto, tente novamente");
  //     }
  // }

  @PostMapping(value="")
  public ResponseEntity<ResponseObject> getTask(
    @RequestBody @Valid @NotNull DnaRequest dnaRequest
  ) {
    // char[][] dna = {
    //   {'A', 'T', 'A', 'A', 'T'},
    //   {'G', 'C', 'G', 'T', 'A'},
    //   {'T', 'G', 'T', 'A', 'T'},
    //   {'T', 'T', 'A', 'T', 'T'},
    //   {'G', 'A', 'T', 'T', 'T'}};
    if (dnaRequest.getDna() == null) {
      return erro("Campo dna não encontrado na requisição");
    } else if (dnaRequest.getDna().length < 4) {
      return erro("Informe uma matriz com tamanho mínimo de 4 linhas");
    } else if (!validNumberOfColumns(dnaRequest.getDna())) {
      return erro("Informe uma matriz com tamanho mínimo de 4 colunas");
    }

    String retorno = isSimian(dnaRequest.getDna()) ? "Simius" : "Human";
    return ok(retorno);
  }

  private boolean validNumberOfColumns(String[] dna) {
    for (int i = 0; i < dna.length; i++) {
      if (dna[i].length() < 4) {
        return false;
      }
    }
    return true;
  }

  private boolean isSimian(String[] dna) {
    int counter = 0;
    counter = wordOccurs(dna, "AAAA", counter);
    if (counter > 1) {
      return true;
    }
    counter = wordOccurs(dna, "TTTT", counter);
    if (counter > 1) {
      return true;
    }
    counter = wordOccurs(dna, "GGGG", counter);
    if (counter > 1) {
      return true;
    }
    counter = wordOccurs(dna, "CCCC", counter);
    if (counter > 1) {
      return true;
    }
    return false;
  }

  private int wordOccurs(String[] dna, String word, int currCounter) {
    for(int i=0; i < dna.length; i++) {
      for(int j=0; j < dna[0].length(); j++) {
        if (searchWord(dna, i, j, word)) {
          if (++currCounter == 2) {
            return currCounter;
          }
        }
      }
    }
    return currCounter;
  }

  boolean searchWord(String[] dna, int row, int col, String word) {
    // All possible directions for each cell (8 directions)
    int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };  
    int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };
    int m = dna.length;
    int n = dna[0].length();

    if (dna[row].charAt(col) != word.charAt(0)) {
      return false;
    }

    int len = word.length(); 
    
    for (int dir = 0; dir < 8; dir++) { 
      // starting point
      int k, rowDirection = row + x[dir];
      int columnDirection = col + y[dir];

      for (k = 1; k < len; k++) {
          // If out of bound break 
          if (rowDirection >= m || rowDirection < 0 || columnDirection >= n || columnDirection < 0)
              break; 

          if (dna[rowDirection].charAt(columnDirection) != word.charAt(k))
              break;
          
          // Moving in particular direction
          rowDirection += x[dir];
          columnDirection += y[dir]; 
      }

      // word found
      if (k == len) {
        return true;
      }
    }
    return false;
  }
}