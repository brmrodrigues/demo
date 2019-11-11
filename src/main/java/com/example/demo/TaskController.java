package com.example.demo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "api/v1/simian")
public class TaskController extends BaseController {
  @PostMapping(value="")
  public ResponseEntity<ResponseObject> getTask(
    @RequestBody @Valid @NotNull DnaRequest dnaRequest
  ) {
    if (dnaRequest.getDna() == null) {
      return erro("Campo dna não encontrado na requisição");
    } else if (dnaRequest.getDna().length < 4) {
      return erro("Informe uma matriz com tamanho mínimo de 4 linhas");
    } else if (!validNumberOfColumns(dnaRequest.getDna())) {
      return erro("Informe uma matriz com tamanho mínimo de 4 colunas");
    }
    String[] req = dnaRequest.getDna();
    char[][] dna = new char[req.length][req.length];
    for (int i = 0; i < req.length; i++) {
      for (int j = 0; j < req.length; j++) {
        if (req[i].charAt(j) != 'A' &&
          req[i].charAt(j) != 'G' &&
          req[i].charAt(j) != 'C' &&
          req[i].charAt(j) != 'T') {
            return erro("Infor uma matriz com valores válidos: A, G, C, T"); 
          }
          dna[i][j] = req[i].charAt(j);
      }
    }
    String retorno = isSimian(dna) ? "Simius" : "Human";
    return ok(retorno);
  }

  private boolean validNumberOfColumns(String[] dna) {
    for (int i = 0; i < dna.length; i++) {
      if (dna[i].length() != dna.length) {
        return false;
      }
    }
    return true;
  }

  private boolean isSimian(char[][] dna) {
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

  private int wordOccurs(char[][] dna, String word, int currCounter) {
    for(int i=0; i < dna.length; i++) {
      for(int j=0; j < dna[0].length; j++) {
        if (searchWord(dna, i, j, word)) {
          if (++currCounter == 2) {
            return currCounter;
          }
        }
      }
    }
    return currCounter;
  }

  boolean searchWord(char[][] dna, int row, int col, String word) {
    // All possible directions for each cell (8 directions)
    int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };  
    int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };
    int m = dna.length;
    int n = dna[0].length;

    if (dna[row][col] != word.charAt(0)) {
      return false;
    }
    dna[row][col] = '#';
    int len = word.length(); 
    
    for (int dir = 0; dir < 8; dir++) { 
      // starting point
      int k, rowDirection = row + x[dir];
      int columnDirection = col + y[dir];

      for (k = 1; k < len; k++) {
          // If out of bound break 
          if (rowDirection >= m || rowDirection < 0 || columnDirection >= n || columnDirection < 0)
              break; 

          if (dna[rowDirection][columnDirection] != word.charAt(k))
              break;
          
          // Moving in particular direction
          dna[rowDirection][columnDirection] = '#';
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