import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import ProductCard from './components/ProductCard';
import './styles.scss';
import { makeRequest } from '../../core/utils/request';
import { ProductsResponse } from '../../core/types/Product';
import ProductCardLoader from './components/Loaders/ProductCardLoader';

const   Catalog = () => {
    //1 - quando o componente iniciar, buscar a lista de produtos
    const[productsResponse, setProductResponse] = useState<ProductsResponse>();
    const [isLoading, setIsLoading] = useState(false);

    //2 - quando a lista de produtos estiver disponível,
    //popular um estado no comonente e listar os produtos dinamicamente.

   // console.log(productsResponse);

    useEffect(() => {
        const params = {
            page: 0,
            linesPerPage: 12 
        }

        setIsLoading(true);
        makeRequest({url: '/products', params })
        .then(response => setProductResponse(response.data))
        .finally(() =>{
            setIsLoading(false);
        })
    }, []);

    return (
        <div className="catalog-container">
            <h1 className="catalog-title">
                Catalogo de produtos
            </h1>
            <div className="catalog-products">
                {isLoading ? <ProductCardLoader/> : (
                     productsResponse?.content.map(product =>(
                        <Link to={`/products/${product.id}`} key={product.id}>
                            <ProductCard product={product}/>
                        </Link>
                    ))
                )}
               
            </div>    
        </div>     
    );
}

export default Catalog;