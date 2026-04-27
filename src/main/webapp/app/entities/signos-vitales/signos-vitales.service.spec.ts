import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { SignosVitales } from '@/shared/model/signos-vitales.model';

import SignosVitalesService from './signos-vitales.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('SignosVitales Service', () => {
    let service: SignosVitalesService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new SignosVitalesService();
      currentDate = new Date();
      elemDefault = new SignosVitales(123, currentDate, 0, 0, 0, 'AAAAAAA', 0, 0, 0, 'AAAAAAA', false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaHoraRegistro: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a SignosVitales', async () => {
        const returnedFromService = {
          id: 123,
          fechaHoraRegistro: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaHoraRegistro: currentDate, fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a SignosVitales', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a SignosVitales', async () => {
        const returnedFromService = {
          fechaHoraRegistro: dayjs(currentDate).format(DATE_TIME_FORMAT),
          peso: 1,
          talla: 1,
          temperatura: 1,
          presionArterial: 'BBBBBB',
          frecuenciaCardiaca: 1,
          frecuenciaRespiratoria: 1,
          saturacionOxigeno: 1,
          observaciones: 'BBBBBB',
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = { fechaHoraRegistro: currentDate, fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a SignosVitales', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a SignosVitales', async () => {
        const patchObject = {
          talla: 1,
          temperatura: 1,
          frecuenciaCardiaca: 1,
          frecuenciaRespiratoria: 1,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...new SignosVitales(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaHoraRegistro: currentDate, fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a SignosVitales', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of SignosVitales', async () => {
        const returnedFromService = {
          fechaHoraRegistro: dayjs(currentDate).format(DATE_TIME_FORMAT),
          peso: 1,
          talla: 1,
          temperatura: 1,
          presionArterial: 'BBBBBB',
          frecuenciaCardiaca: 1,
          frecuenciaRespiratoria: 1,
          saturacionOxigeno: 1,
          observaciones: 'BBBBBB',
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaHoraRegistro: currentDate, fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of SignosVitales', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a SignosVitales', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a SignosVitales', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
