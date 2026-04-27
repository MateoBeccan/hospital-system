import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Tratamiento } from '@/shared/model/tratamiento.model';

import TratamientoService from './tratamiento.service';

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
  describe('Tratamiento Service', () => {
    let service: TratamientoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TratamientoService();
      currentDate = new Date();
      elemDefault = new Tratamiento(
        123,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        currentDate,
        false,
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          fechaProximaRevision: dayjs(currentDate).format(DATE_FORMAT),
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

      it('should create a Tratamiento', async () => {
        const returnedFromService = {
          id: 123,
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          fechaProximaRevision: dayjs(currentDate).format(DATE_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaInicio: currentDate,
          fechaFin: currentDate,
          fechaProximaRevision: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Tratamiento', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Tratamiento', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          descripcion: 'BBBBBB',
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          observaciones: 'BBBBBB',
          fechaProximaRevision: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = {
          fechaInicio: currentDate,
          fechaFin: currentDate,
          fechaProximaRevision: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Tratamiento', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Tratamiento', async () => {
        const patchObject = { codigo: 'BBBBBB', fechaFin: dayjs(currentDate).format(DATE_FORMAT), ...new Tratamiento() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          fechaInicio: currentDate,
          fechaFin: currentDate,
          fechaProximaRevision: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Tratamiento', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Tratamiento', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          descripcion: 'BBBBBB',
          fechaInicio: dayjs(currentDate).format(DATE_FORMAT),
          fechaFin: dayjs(currentDate).format(DATE_FORMAT),
          observaciones: 'BBBBBB',
          fechaProximaRevision: dayjs(currentDate).format(DATE_FORMAT),
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaInicio: currentDate,
          fechaFin: currentDate,
          fechaProximaRevision: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Tratamiento', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Tratamiento', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Tratamiento', async () => {
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
