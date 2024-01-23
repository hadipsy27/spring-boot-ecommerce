import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list-grid.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent {

  products: Product[] = [];
  currentCategoryId: number = 1;

  constructor(private productService: ProductService, private route: ActivatedRoute){}

  ngOnInit(): void{
    this.route.paramMap.subscribe(() => {
      this.listProducts();
    })
  }

  listProducts() {

    // Retrieve the 'id' parameter from the route
    const categoryIdParam = this.route.snapshot.paramMap.get('id');

    // Check if categoryIdParam is not null or undefined
    if (categoryIdParam !== null && categoryIdParam !== undefined) {
      // Convert the categoryIdParam to a number using the "+" symbol
      this.currentCategoryId = +categoryIdParam;
    } else {
      // If 'id' parameter is not found, default to category id 1
      this.currentCategoryId = 1;
    }
    
    this.productService.getProductList(this.currentCategoryId).subscribe(
      data => {
        this.products = data;
      }
    )
  }

}
